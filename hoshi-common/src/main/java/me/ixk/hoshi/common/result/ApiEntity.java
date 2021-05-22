package me.ixk.hoshi.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.OffsetDateTime;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:33
 */
@Data
public class ApiEntity<T> {

    private final Integer status;
    private final String message;
    private final OffsetDateTime timestamp = OffsetDateTime.now();

    @JsonInclude(Include.NON_NULL)
    private final T data;

    @JsonInclude(Include.NON_NULL)
    private final String error;

    public ApiEntity(@NotNull final Integer status, @NotNull final String message, @Nullable final T data) {
        Assert.notNull(status, "code 必须设置（规范）");
        Assert.notNull(message, "Message 必须设置（规范）");
        this.status = status;
        this.message = message;
        this.data = data;
        if (status >= 400 && status < 600) {
            final HttpStatus resolve = HttpStatus.resolve(status);
            this.error =
                resolve == null ? HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() : resolve.getReasonPhrase();
        } else {
            this.error = null;
        }
    }
}
