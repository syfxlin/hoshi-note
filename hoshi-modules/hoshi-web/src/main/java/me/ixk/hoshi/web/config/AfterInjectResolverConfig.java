/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.config;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ixk.hoshi.web.resolver.ApiResultReturnValueHandler;
import me.ixk.hoshi.web.resolver.ModelArgumentResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.servlet.mvc.method.annotation.*;

/**
 * 配置 {@link ModelArgumentResolver} 和 {@link ApiResultReturnValueHandler}
 * <p>
 * 由于部分请求处理器需要使用 {@link HttpMessageConverter} 等依赖，必须在 {@link RequestMappingHandlerAdapter} 初始化完毕才可添加
 *
 * @author Otstar Lin
 * @date 2021/5/26 16:57
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AfterInjectResolverConfig implements InitializingBean {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 获取 {@link RequestBodyAdvice} 和 {@link ResponseBodyAdvice} 实例列表
     *
     * @return 实例列表
     */
    private List<Object> requestResponseBodyAdvice() {
        final ApplicationContext applicationContext = this.requestMappingHandlerAdapter.getApplicationContext();
        if (applicationContext == null) {
            return new ArrayList<>();
        }
        final List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(applicationContext);
        final List<Object> requestResponseBodyAdviceBeans = new ArrayList<>();
        requestResponseBodyAdviceBeans.add(new JsonViewRequestBodyAdvice());
        requestResponseBodyAdviceBeans.add(new JsonViewResponseBodyAdvice());
        for (final ControllerAdviceBean adviceBean : adviceBeans) {
            final Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
            }
            if (
                RequestBodyAdvice.class.isAssignableFrom(beanType) ||
                ResponseBodyAdvice.class.isAssignableFrom(beanType)
            ) {
                requestResponseBodyAdviceBeans.add(adviceBean);
            }
        }
        return requestResponseBodyAdviceBeans;
    }

    @Override
    public void afterPropertiesSet() {
        final List<HttpMessageConverter<?>> converters = this.requestMappingHandlerAdapter.getMessageConverters();
        final List<Object> responseBodyAdvice = this.requestResponseBodyAdvice();
        this.requestMappingHandlerAdapter.setArgumentResolvers(
                this.insert(
                        this.requestMappingHandlerAdapter.getArgumentResolvers(),
                        new ModelArgumentResolver(converters, responseBodyAdvice)
                    )
            );
        this.requestMappingHandlerAdapter.setReturnValueHandlers(
                this.insert(
                        this.requestMappingHandlerAdapter.getReturnValueHandlers(),
                        new ApiResultReturnValueHandler(converters, responseBodyAdvice)
                    )
            );
        if (log.isTraceEnabled()) {
            log.trace("添加 ModelArgumentResolver 和 ApiResultReturnValueHandler");
        }
    }

    private <T> List<T> insert(final List<T> list, final T item) {
        final List<T> result = new ArrayList<>();
        result.add(item);
        if (list != null) {
            result.addAll(list);
        }
        return result;
    }
}
