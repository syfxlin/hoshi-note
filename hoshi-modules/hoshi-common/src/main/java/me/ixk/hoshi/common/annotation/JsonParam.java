/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.annotation;

import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;

/**
 * @author Otstar Lin
 * @date 2021/5/22 17:41
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonParam {
    @AliasFor("path")
    String value() default "";

    @AliasFor("value")
    String path() default "";

    boolean required() default true;

    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
