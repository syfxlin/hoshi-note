/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("统一响应实体")
public class ApiEntity<T> {

    @ApiModelProperty("响应码（与 Http 的响应码相同）")
    private final Integer status;

    @ApiModelProperty("响应信息（用于描述操作的成功信息或失败原因）")
    private final String message;

    @ApiModelProperty("时间（ISO 8601）")
    private final OffsetDateTime timestamp = OffsetDateTime.now();

    @ApiModelProperty("实际的响应数据")
    @JsonInclude(Include.NON_NULL)
    private final T data;

    @ApiModelProperty("错误信息（与 Http 的响应信息相同）")
    @JsonInclude(Include.NON_NULL)
    private final String error;

    public ApiEntity(@NotNull final Integer status, @NotNull final String message, @Nullable final T data) {
        Assert.notNull(status, "Status Code 必须设置（规范）");
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
