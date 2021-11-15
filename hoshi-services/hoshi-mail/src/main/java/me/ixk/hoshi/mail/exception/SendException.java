package me.ixk.hoshi.mail.exception;

/**
 * @author Otstar Lin
 * @date 2021/7/18 21:47
 */
public class SendException extends RuntimeException {

    public SendException(final Throwable cause) {
        super(cause);
    }

    public SendException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
