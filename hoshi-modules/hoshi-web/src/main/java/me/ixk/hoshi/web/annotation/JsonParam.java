/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.annotation;

import java.lang.annotation.*;
import me.ixk.hoshi.web.resolver.JsonArgumentResolver;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 注入单个 Json 参数
 * <p>
 * 与 {@link RequestParam} 类似，用于控制器参数，注入单个 Json 参数，使用 JsonPath 语法进行解析
 *
 * @author Otstar Lin
 * @date 2021/5/22 17:41
 * @see JsonArgumentResolver
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonParam {
    /**
     * JsonPath 路径
     * <p>
     * 别名 {@link #path()}
     *
     * @return 路径
     */
    @AliasFor("path")
    String value() default "";

    /**
     * JsonPath 路径
     *
     * @return 路径
     */
    @AliasFor("value")
    String path() default "";

    /**
     * 是否必须注入
     *
     * @return 是否必须注入
     */
    boolean required() default true;

    /**
     * 默认值
     *
     * @return 默认值
     */
    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
