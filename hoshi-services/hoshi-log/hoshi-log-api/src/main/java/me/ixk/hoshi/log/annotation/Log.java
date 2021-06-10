package me.ixk.hoshi.log.annotation;

import java.lang.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/6/10 18:50
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String type();

    String operate();

    String message() default "";
}
