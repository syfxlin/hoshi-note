/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.annotation;

import java.lang.annotation.*;

/**
 * 注入用户 ID，该 ID 从 Session 取得
 *
 * @author Otstar Lin
 * @date 2021/5/28 22:54
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserId {
}
