/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.json;

import com.fasterxml.jackson.annotation.JsonView;
import java.lang.annotation.*;

/**
 * Json 字段模式
 * <p>
 * 与 {@link JsonView} 类似，用于过滤字段
 *
 * @author Otstar Lin
 * @date 2021/6/1 14:27
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonMode {
    Class<?>[] value() default {};

    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Exclude {
    }

    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Include {
    }
}
