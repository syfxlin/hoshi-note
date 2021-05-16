package me.ixk.hoshi.common.exception;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 11:40
 */
public class JsonException extends RuntimeException {

    private static final long serialVersionUID = -702953254862741906L;

    public JsonException(final Throwable cause) {
        super(cause);
    }
}