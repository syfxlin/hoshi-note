package me.ixk.hoshi.mail.exception;

/**
 * @author Otstar Lin
 * @date 2021/7/21 17:05
 */
public class RateLimitException extends RuntimeException {

    private static final long serialVersionUID = -618724454181779908L;

    public RateLimitException(final String message) {
        super(message);
    }
}
