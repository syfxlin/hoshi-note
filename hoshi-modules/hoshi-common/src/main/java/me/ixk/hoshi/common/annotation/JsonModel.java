/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.annotation;

import java.lang.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/26 16:01
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonModel {
    boolean required() default true;

    boolean skipBind() default false;
}
