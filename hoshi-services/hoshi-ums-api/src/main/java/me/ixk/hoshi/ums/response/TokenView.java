/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/18 16:10
 */
@Data
@Builder
@Schema(name = "令牌")
public class TokenView {

    @Schema(name = "Token ID")
    private final Long id;

    @Schema(name = "Token")
    private final String token;

    @Schema(name = "名称")
    private final String name;
}
