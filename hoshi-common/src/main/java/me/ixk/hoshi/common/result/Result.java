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

    public static <T> Result<T> data(final T data) {
        return data(2000, "操作成功", data);
    }

    public static <T> Result<T> data(final int code, final T data) {
        return data(code, "操作成功", data);
    }

    public static <T> Result<T> data(final String message, final T data) {
        return data(2000, message, data);
    }

    public static <T> Result<T> data(final int code, final String message, final T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error(final int code, final String message) {
        return data(code, message, null);
    }
}
