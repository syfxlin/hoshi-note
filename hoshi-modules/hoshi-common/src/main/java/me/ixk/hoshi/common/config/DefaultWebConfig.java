package me.ixk.hoshi.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.resolver.JsonArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Otstar Lin
 * @date 2021/5/22 19:16
 */
@Configuration
@RequiredArgsConstructor
public class DefaultWebConfig implements WebMvcConfigurer {

    private final ConversionService conversionService;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JsonArgumentResolver(this.conversionService));
    }
}
