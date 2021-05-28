/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.annotation;

import java.lang.annotation.*;
import me.ixk.hoshi.common.result.ApiMessage;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 3:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface ApiResultBody {
    /**
     * 状态
     *
     * @return 状态
     */
    int status() default -1;

    /**
     * 信息
     *
     * @return 信息
     */
    String message() default "";

    /**
     * 状态信息
     *
     * @return 状态信息
     */
    ApiMessage apiMessage() default ApiMessage.OK;
}
