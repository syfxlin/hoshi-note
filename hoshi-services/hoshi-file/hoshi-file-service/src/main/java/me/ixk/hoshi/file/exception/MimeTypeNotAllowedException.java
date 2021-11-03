/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.exception;

/**
 * @author Otstar Lin
 * @date 2021/5/27 12:36
 */
public class MimeTypeNotAllowedException extends StorageException {

    public MimeTypeNotAllowedException(final String mimeType) {
        super("上传的文件类型 [" + mimeType + "] 不被允许");
    }
}
