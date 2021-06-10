/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.annotation;

import java.lang.annotation.*;
import me.ixk.hoshi.common.resolver.ApiResultReturnValueHandler;
import me.ixk.hoshi.common.result.ApiEntity;
import me.ixk.hoshi.common.result.ApiMessage;
import me.ixk.hoshi.common.result.ApiResult;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 将返回值包装成 {@link ApiResult}（{@link ApiEntity}）
 * <p>
 * 和 {@link ResponseBody} 类似，标注于控制器方法上，{@link ApiResultReturnValueHandler} 会将响应包装成 {@link ApiEntity}
 * <p>
 * 建议直接返回 {@link ApiResult} 以使用更多功能
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 3:11
 * @see ApiResultReturnValueHandler
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
     * <p>
     * 该设置会被 {@link #status()} 和 {@link #message()} 覆盖，若其中有未设置的则会使用该设置
     *
     * @return 状态信息
     */
    ApiMessage apiMessage() default ApiMessage.OK;
}
