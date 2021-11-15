/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.annotation;

import java.lang.annotation.*;
import me.ixk.hoshi.api.config.DefaultFeignConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;

/**
 * 启用客户端
 *
 * @author Otstar Lin
 * @date 2021/6/8 15:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableFeignClients(defaultConfiguration = { DefaultFeignConfig.class })
public @interface EnableClient {
    @AliasFor(annotation = EnableFeignClients.class)
    String[] value() default {};

    @AliasFor(annotation = EnableFeignClients.class)
    String[] basePackages() default {};

    @AliasFor(annotation = EnableFeignClients.class)
    Class<?>[] basePackageClasses() default {};

    @AliasFor(annotation = EnableFeignClients.class)
    Class<?>[] defaultConfiguration() default {};

    @AliasFor(annotation = EnableFeignClients.class)
    Class<?>[] clients() default {};
}
