package me.ixk.hoshi.common.result;

import java.util.Collections;
import java.util.List;

/**
 * @author Otstar Lin
 * @date 2021/5/22 16:15
 */
public class ApiBindException extends RuntimeException {

    private static final long serialVersionUID = -811998334758938014L;
    private final List<String> errors;

    public ApiBindException(final String message) {
        this(message, Collections.emptyList());
    }

    public ApiBindException(final String message, final Throwable cause) {
        super(message, cause);
        this.errors = Collections.emptyList();
    }

    public ApiBindException(final Throwable cause) {
        super(cause);
        this.errors = Collections.emptyList();
    }

    public ApiBindException(final String message, final List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public ApiBindException(final String message, final Throwable cause, final List<String> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public ApiBindException(final Throwable cause, final List<String> errors) {
        super(cause);
        this.errors = errors;
    }

    public ApiBindException(final List<String> errors) {
        super();
        this.errors = errors;
    }

    public List<String> getErrors() {
        return this.errors;
    }
}
