package me.ixk.hoshi.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 标准返回
 *
 * @author Otstar Lin
 * @date 2021/5/15 下午 11:49
 */
@Data
@AllArgsConstructor
public class Result<T> {

    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> Result<T> data(final ResultCode code, final T data) {
        return data(code.getCode(), code.getMessage(), data);
    }

    public static <T> Result<T> data(final T data) {
        return data(ResultCode.OK, data);
    }

    public static <T> Result<T> data(final int code, final T data) {
        return data(code, ResultCode.OK.getMessage(), data);
    }

    public static <T> Result<T> data(final String message, final T data) {
        return data(ResultCode.OK.getCode(), message, data);
    }

    public static <T> Result<T> data(final int code, final String message, final T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error(final int code, final String message) {
        return error(code, message, null);
    }

    public static <T> Result<T> error(final ResultCode code) {
        return error(code.getCode(), code.getMessage());
    }

    public static <T> Result<T> error(final ResultCode code, final T data) {
        return error(code.getCode(), code.getMessage(), data);
    }

    public static <T> Result<T> error(final int code, final String message, final T data) {
        return data(code, message, data);
    }
}
