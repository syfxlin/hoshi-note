package me.ixk.hoshi.file.exception;

/**
 * @author Otstar Lin
 * @date 2021/5/23 9:36
 */
public class StorageException extends RuntimeException {

    private static final long serialVersionUID = 2872097101589585064L;

    public StorageException(final String message) {
        super(message);
    }

    public StorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
