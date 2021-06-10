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
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:33
 */
@Data
@NoArgsConstructor
@ApiModel("统一响应实体")
public class ApiEntity<T> {

    @ApiModelProperty("响应码（与 Http 的响应码相同）")
    private Integer status;

    @ApiModelProperty("响应信息（用于描述操作的成功信息或失败原因）")
    private String message;

    @ApiModelProperty("时间（ISO 8601）")
    private OffsetDateTime timestamp;

    @ApiModelProperty("实际的响应数据")
    @JsonInclude(Include.NON_NULL)
    private T data;

    @ApiModelProperty("响应码对应的信息")
    private String reason;

    public ApiEntity(@NotNull final Integer status, @NotNull final String message, @Nullable final T data) {
        Assert.notNull(status, "Status Code 必须设置（规范）");
        Assert.notNull(message, "Message 必须设置（规范）");
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = OffsetDateTime.now();
        final ApiMessage apiMessage = ApiMessage.resolve(status);
        this.reason = apiMessage == null ? null : apiMessage.message();
    }
}
