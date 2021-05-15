package me.ixk.hoshi.common.config;

import feign.RequestInterceptor;
import me.ixk.hoshi.common.util.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Otstar Lin
 * @date 2021/5/9 下午 2:52
 */
@Configuration
public class DefaultFeignConfig {

    @Bean
    public RequestInterceptor sessionRequestInterceptor() {
        return Request::wrapperHeaders;
    }
}
