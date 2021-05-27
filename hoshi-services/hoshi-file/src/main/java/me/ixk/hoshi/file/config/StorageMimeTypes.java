package me.ixk.hoshi.file.config;

import java.util.Set;
import me.ixk.hoshi.file.exception.MimeTypeNotAllowedException;

/**
 * @author Otstar Lin
 * @date 2021/5/27 12:24
 */
public class StorageMimeTypes {

    private final Set<String> mimeTypes;

    public StorageMimeTypes(final Set<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public boolean allow(final String mimeType) {
        return this.mimeTypes.contains(mimeType);
    }

    public void valid(final String mimeType) {
        if (!this.allow(mimeType)) {
            throw new MimeTypeNotAllowedException(mimeType);
        }
    }
}
