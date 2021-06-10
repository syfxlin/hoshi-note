/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.client.util;

import feign.RequestTemplate;
import java.util.function.BiConsumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.ixk.hoshi.common.exception.UnsupportedInstantiationException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求工具类
 *
 * @author Otstar Lin
 * @date 2021/5/9 下午 3:38
 */
public final class Request {

    private Request() throws UnsupportedInstantiationException {
        throw new UnsupportedInstantiationException(Request.class);
    }

    /**
     * 获取当前 {@link ServletRequestAttributes}
     * <p>
     * {@link ThreadLocal} 线程隔离，请求线程中有效
     *
     * @return {@link ServletRequestAttributes}
     */
    public static ServletRequestAttributes attributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取当前 {@link HttpServletRequest}
     * <p>
     * {@link ThreadLocal} 线程隔离，请求线程中有效
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest request() {
        final ServletRequestAttributes attributes = attributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    /**
     * 获取当前 {@link HttpServletResponse}
     * <p>
     * {@link ThreadLocal} 线程隔离，请求线程中有效
     *
     * @return {@link HttpServletResponse}
     */
    public static HttpServletResponse response() {
        final ServletRequestAttributes attributes = attributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getResponse();
    }

    /**
     * 消费认证 Token
     * <p>
     * 从 {@link HttpServletRequest} 中读取，{@link ThreadLocal} 线程隔离，请求线程中有效
     *
     * @param consumer Token 消费者（Token 名称，Token 值）
     */
    public static void token(final BiConsumer<String, String> consumer) {
        final String token = token();
        if (token != null) {
            consumer.accept("Authorization", token);
        }
    }

    /**
     * 添加认证 Token 拦截器
     * <p>
     * 拦截器会在每次使用 {@link RestTemplate} 时从 {@link HttpServletRequest} 中读取认证 Token 并设置
     * <p>
     * {@link ThreadLocal} 线程隔离，请求线程中有效
     *
     * @param restTemplate {@link RestTemplate}
     * @return 添加拦截器后的 {@link RestTemplate}
     */
    public static RestTemplate addToken(final RestTemplate restTemplate) {
        final HttpServletRequest request = request();
        if (request == null) {
            return restTemplate;
        }
        restTemplate
            .getInterceptors()
            .add(
                (req, body, execution) -> {
                    token(req.getHeaders()::add);
                    return execution.execute(req, body);
                }
            );
        return restTemplate;
    }

    /**
     * 添加认证 Token 拦截器
     * <p>
     * 拦截器会在每次使用 {@link RequestTemplate}（Feign 请求）时从 {@link HttpServletRequest} 中读取认证 Token 并设置
     * <p>
     * {@link ThreadLocal} 线程隔离，请求线程中有效
     *
     * @param requestTemplate {@link RequestTemplate}
     * @return 添加拦截器后的 {@link RequestTemplate}
     */
    public static RequestTemplate addToken(final RequestTemplate requestTemplate) {
        final HttpServletRequest request = request();
        if (request == null) {
            return requestTemplate;
        }
        token(requestTemplate::header);
        return requestTemplate;
    }

    /**
     * 获取认证 Token
     * <p>
     * 从 {@link HttpServletRequest} 获取认证 Token，{@link ThreadLocal} 线程隔离，请求线程中有效
     *
     * @return 认证 Token，当 Token 不存在或者不在请求线程时返回 {@code null}
     */
    public static String token() {
        final HttpServletRequest request = request();
        if (request == null) {
            return null;
        }
        return request.getHeader("Authorization");
    }

    /**
     * 获取访问者 IP
     *
     * @return IP，当不处于请求线程时返回 {@code null}
     */
    public static String ip() {
        final HttpServletRequest request = request();
        if (request == null) {
            return null;
        }
        return request.getRemoteAddr();
    }
}
