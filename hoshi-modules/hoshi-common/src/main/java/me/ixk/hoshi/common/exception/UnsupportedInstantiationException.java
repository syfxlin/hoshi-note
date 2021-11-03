/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.exception;

import org.jetbrains.annotations.NotNull;

/**
 * 不支持实例化
 * <p>
 * 用于工具类
 *
 * @author Otstar Lin
 * @date 2021/5/9 下午 3:39
 */
public class UnsupportedInstantiationException extends Exception {

    public UnsupportedInstantiationException(@NotNull final Class<?> target) {
        super(target.getName());
    }
}
