package me.ixk.hoshi.file.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.ixk.hoshi.common.exception.UnsupportedInstantiationException;
import me.ixk.hoshi.file.exception.StorageException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

/**
 * @author Otstar Lin
 * @date 2021/5/23 17:18
 */
public final class Mime {

    private static final MimeTypes MIME_TYPES = MimeTypes.getDefaultMimeTypes();

    private Mime() throws UnsupportedInstantiationException {
        throw new UnsupportedInstantiationException(Mime.class);
    }

    public static MediaType media(InputStream in, final String filename) {
        try {
            if (!(in instanceof BufferedInputStream)) {
                in = new BufferedInputStream(in);
            }
            final Metadata md = new Metadata();
            md.add(Metadata.RESOURCE_NAME_KEY, filename);
            return MIME_TYPES.detect(in, md);
        } catch (final IOException e) {
            throw new StorageException("无法解析类型", e);
        }
    }

    public static MimeType mime(final String mediaType) {
        try {
            return MIME_TYPES.forName(mediaType);
        } catch (final MimeTypeException e) {
            throw new StorageException("无法解析类型", e);
        }
    }

    public static MimeType mime(final MediaType mediaType) {
        return mime(mediaType.toString());
    }
}
