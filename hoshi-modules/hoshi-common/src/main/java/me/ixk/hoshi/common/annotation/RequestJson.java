package me.ixk.hoshi.common.annotation;

import java.lang.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/22 17:42
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJson {
}
