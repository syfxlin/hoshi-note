/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.json;

import java.lang.annotation.*;

/**
 * 激活 {@link JsonMode}
 *
 * @author Otstar Lin
 * @date 2021/6/1 14:33
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonActive {
    Class<?> value();
}
