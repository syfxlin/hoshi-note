/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 11:40
 */
public class JsonException extends RuntimeException {

    private static final long serialVersionUID = -702953254862741906L;

    public JsonException(@NotNull final Throwable cause) {
        super(cause);
    }
}
