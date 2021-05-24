package me.ixk.hoshi.file.view;

import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/5/24 21:29
 */
@Data
@Builder
public class FileView {

    private final String fileName;
    private final long size;
    private final String url;
}
