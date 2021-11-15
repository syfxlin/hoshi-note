/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.exception;

import java.io.Serial;

/**
 * @author Otstar Lin
 * @date 2021/11/14 21:22
 */
public class MinioException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3705626949478124053L;

    public MinioException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
