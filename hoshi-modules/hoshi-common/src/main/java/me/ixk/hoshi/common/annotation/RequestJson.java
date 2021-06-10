/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.annotation;

import java.lang.annotation.*;
import me.ixk.hoshi.common.resolver.JsonArgumentResolver;

/**
 * 注入 Json 参数
 * <p>
 * 标注于方法上，无须为参数标注注解即可注入指定的 Json 对象，用于解决 axios 等默认使用 Json 传参快速获取的的问题
 *
 * @author Otstar Lin
 * @date 2021/5/22 17:42
 * @see JsonArgumentResolver
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJson {
}
