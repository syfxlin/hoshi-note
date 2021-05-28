/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.resolver.ApiResultReturnValueHandler;
import me.ixk.hoshi.common.resolver.ModelArgumentResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.servlet.mvc.method.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/26 16:57
 */
@Configuration
@RequiredArgsConstructor
public class AfterInjectResolverConfig implements InitializingBean {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

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
    public void afterPropertiesSet() throws Exception {
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
    }

    private <T> List<T> insert(final List<T> list, final T items) {
        final List<T> result = new ArrayList<>(Collections.singletonList(items));
        if (list != null) {
            result.addAll(list);
        }
        return result;
    }
}
