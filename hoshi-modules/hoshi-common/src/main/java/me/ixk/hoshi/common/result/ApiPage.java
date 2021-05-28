/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页响应")
public class ApiPage<T> {

    @ApiModelProperty("当前页")
    private long page;

    @ApiModelProperty("页大小")
    private long pageSize;

    @ApiModelProperty("页面数量")
    private long pages;

    @ApiModelProperty("数据总量")
    private long total;

    @ApiModelProperty("当前页的数据")
    private List<T> records;

    public ApiPage(@NotNull final Page<T> page) {
        this(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements(), page.getContent());
    }

    public ApiPage(@NotNull final List<T> records) {
        this(1, records.size(), 1, records.size(), records);
    }
}
