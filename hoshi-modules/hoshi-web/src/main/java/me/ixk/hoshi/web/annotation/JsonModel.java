/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.annotation;

import java.lang.annotation.*;
import me.ixk.hoshi.web.resolver.ModelArgumentResolver;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 绑定请求 Body
 * <p>
 * 和 {@link RequestBody} 功能相同，负责将请求内容转换为 Java 对象，同时解决了 {@link RequestBody} 不能将 {@link PathVariable} 等对象绑定到标准的对象上
 *
 * @author Otstar Lin
 * @date 2021/5/26 16:01
 * @see ModelArgumentResolver
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonModel {
    /**
     * 是否必须注入
     *
     * @return 是否必须注入
     */
    boolean required() default true;

    /**
     * 跳过 {@link WebDataBinder} 绑定
     * <p>
     * 如果跳过了绑定则无法将 {@link PathVariable} 等非请求内容的参数绑定到对象内
     *
     * @return 是否跳过
     */
    boolean skipBind() default false;
}
