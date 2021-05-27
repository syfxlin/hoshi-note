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
 * @author Otstar Lin
 * @date 2021/5/9 下午 3:38
 */
public final class Request {

    private Request() throws UnsupportedInstantiationException {
        throw new UnsupportedInstantiationException(Request.class);
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

    public static void addToken(final BiConsumer<String, String> consumer) {
        final HttpServletRequest request = request();
        if (request == null) {
            return;
        }
        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            token = request.getHeader("Authorization");
            if (token != null) {
                token = token.replace("Bearer ", "");
            }
        }
        if (token != null) {
            consumer.accept("X-Auth-Token", token);
        }
    }

    public static RestTemplate addToken(final RestTemplate restTemplate) {
        final HttpServletRequest request = request();
        if (request == null) {
            return restTemplate;
        }
        restTemplate
            .getInterceptors()
            .add(
                (req, body, execution) -> {
                    addToken(req.getHeaders()::add);
                    return execution.execute(req, body);
                }
            );
        return restTemplate;
    }

    public static RequestTemplate addToken(final RequestTemplate requestTemplate) {
        final HttpServletRequest request = request();
        if (request == null) {
            return requestTemplate;
        }
        addToken(requestTemplate::header);
        return requestTemplate;
    }
}
