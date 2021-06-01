/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.view;

import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/5/23 10:27
 */
@Data
@Builder
public class UploadView {

    private final String extName;
    private final String mediaType;
    private final String fileName;
    private final long size;
    private final String url;
}