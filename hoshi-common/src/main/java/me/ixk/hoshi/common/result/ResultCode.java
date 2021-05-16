package me.ixk.hoshi.common.result;

/**
 * 常用统一响应
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 2:56
 */
public enum ResultCode {
    /**
     * 操作成功
     */
    OK(2000, "操作成功"),
    /**
     * 参数错误
     */
    BAD_REQUEST(4001, "参数错误"),
    /**
     * 操作异常
     */
    ERROR(5000, "操作异常");

    private final int code;
    private final String message;

    ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
