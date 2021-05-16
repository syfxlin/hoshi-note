package me.ixk.hoshi.common.annotation;

import java.lang.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 3:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface NoStdResult {
}
