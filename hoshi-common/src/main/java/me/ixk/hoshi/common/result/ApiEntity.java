package me.ixk.hoshi.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:33
 */
@Data
public class ApiEntity<T> {

    private final Integer code;
    private final String msg;
    private final OffsetDateTime time = OffsetDateTime.now();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public ApiEntity(@NotNull final Integer code, @NotNull final String msg, @Nullable final T data) {
        Assert.notNull(code, "code 必须设置（规范）");
        Assert.notNull(msg, "Message 必须设置（规范）");
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
