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

    private final String extname;
    private final String filename;
    private final long size;
    private final String url;
}
