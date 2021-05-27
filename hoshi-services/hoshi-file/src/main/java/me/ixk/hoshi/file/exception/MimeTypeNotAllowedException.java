package me.ixk.hoshi.file.exception;

/**
 * @author Otstar Lin
 * @date 2021/5/27 12:36
 */
public class MimeTypeNotAllowedException extends StorageException {

    private static final long serialVersionUID = -1500196921299383087L;

    public MimeTypeNotAllowedException(final String mimeType) {
        super("上传的文件类型 [" + mimeType + "] 不被允许");
    }
}
