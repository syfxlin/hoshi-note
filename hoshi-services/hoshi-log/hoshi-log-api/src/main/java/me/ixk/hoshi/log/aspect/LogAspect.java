package me.ixk.hoshi.log.aspect;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ClassUtil;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ixk.hoshi.log.annotation.Log;
import me.ixk.hoshi.log.client.LogRemoteService;
import me.ixk.hoshi.log.view.request.AddLogView;
import me.ixk.hoshi.log.view.request.AddLogView.AddLogViewBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Otstar Lin
 * @date 2021/6/10 18:57
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private static final String SECURITY_UTIL_NAME = "me.ixk.hoshi.security.util.Security";
    private static MethodHandle GET_ID_METHOD;

    static {
        try {
            final Class<Object> clazz = ClassUtil.loadClass(SECURITY_UTIL_NAME);
            final MethodType methodType = MethodType.methodType(String.class);
            GET_ID_METHOD = MethodHandles.publicLookup().findStatic(clazz, "id", methodType);
        } catch (final NoSuchMethodException | IllegalAccessException | UtilException e) {
            GET_ID_METHOD = null;
        }
    }

    private final LogRemoteService logRemoteService;
    private final Environment environment;

    @Around("@annotation(me.ixk.hoshi.log.annotation.Log)")
    public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final Log annotation = method.getAnnotation(Log.class);
        String userId;
        try {
            userId = GET_ID_METHOD == null ? null : (String) GET_ID_METHOD.invoke();
        } catch (final Throwable throwable) {
            userId = null;
        }
        final AddLogViewBuilder builder = AddLogView
            .builder()
            .type(annotation.type())
            .operate(annotation.operate())
            .message(annotation.message())
            .module(environment.getProperty("spring.application.name"))
            .ip(this.getIp())
            .method(method.toGenericString())
            .user(userId)
            .startTime(OffsetDateTime.now());
        Object result = null;
        Throwable ex = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
            builder.status(Boolean.TRUE);
        } catch (final Throwable throwable) {
            ex = throwable;
            builder.status(Boolean.FALSE);
        }
        final AddLogView logView = builder.endTime(OffsetDateTime.now()).build();
        logRemoteService.add(logView);
        if (ex != null) {
            throw ex;
        }
        return result;
    }

    private String getIp() {
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        final HttpServletRequest request = attributes.getRequest();
        return request.getRemoteAddr();
    }
}
