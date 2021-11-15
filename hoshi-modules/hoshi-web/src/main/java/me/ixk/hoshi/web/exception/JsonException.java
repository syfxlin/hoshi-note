/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.exception;

import java.io.Serial;
import org.jetbrains.annotations.NotNull;

/**
 * Json 处理异常
 *
 * @author Otstar Lin
 * @date 2021/5/15 下午 11:40
 */
public class JsonException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1994426102961757115L;

    public JsonException(@NotNull final Throwable cause) {
        super(cause);
    }
}
