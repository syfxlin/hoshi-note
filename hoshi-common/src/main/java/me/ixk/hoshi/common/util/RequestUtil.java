package me.ixk.hoshi.common.util;

import feign.RequestTemplate;
import java.util.Collections;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.ixk.hoshi.common.exception.UnsupportedInstantiationException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Otstar Lin
 * @date 2021/5/9 下午 3:38
 */
public final class RequestUtil {

    private RequestUtil() throws UnsupportedInstantiationException {
        throw new UnsupportedInstantiationException(RequestUtil.class);
    }

    public static ServletRequestAttributes attributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest request() {
        final ServletRequestAttributes attributes = attributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    public static HttpServletResponse response() {
        final ServletRequestAttributes attributes = attributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getResponse();
    }

    public static void wrapperHeaders(final RestTemplate restTemplate) {
        final HttpServletRequest request = request();
        if (request == null) {
            return;
        }
        restTemplate
            .getInterceptors()
            .add(
                (req, body, execution) -> {
                    final Enumeration<String> headerNames = request.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        final String headerName = headerNames.nextElement();
                        req.getHeaders().addAll(headerName, Collections.list(request.getHeaders(headerName)));
                    }
                    return execution.execute(req, body);
                }
            );
    }

    public static void wrapperHeaders(final RequestTemplate requestTemplate) {
        final HttpServletRequest request = request();
        if (request == null) {
            return;
        }
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            final Iterable<String> headerValues = () -> request.getHeaders(headerName).asIterator();
            requestTemplate.header(headerName, headerValues);
        }
    }
}
