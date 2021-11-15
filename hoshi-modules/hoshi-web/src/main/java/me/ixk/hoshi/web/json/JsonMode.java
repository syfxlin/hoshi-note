/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.json;

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
    /**
     * 模式
     * <p>
     * 当前字段包含的模式，可使用 @JsonActive 启用，当包含 @JsonActive 的子类的时候也会生效
     *
     * @return 模式
     */
    Class<?>[] value() default {};

    /**
     * 排除模式
     * <p>
     * 默认使用的模式，在该模式下，如果模式匹配，则将字段排除
     */
    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Exclude {
    }

    /**
     * 包含模式
     * <p>
     * 在该模式下，如果模式匹配，才会将该字段包含，不匹配的则排除
     */
    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Include {
    }
}
