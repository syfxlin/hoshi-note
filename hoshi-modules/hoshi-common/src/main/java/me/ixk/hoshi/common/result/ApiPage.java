/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Api 分页实体
 * <p>
 * 规范分页响应的格式
 *
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "分页")
public class ApiPage<T> {

    @Schema(name = "当前页")
    private long page;

    @Schema(name = "页大小")
    private long size;

    @Schema(name = "页面数量")
    private long pages;

    @Schema(name = "数据总量")
    private long total;

    @Schema(name = "当前页的数据")
    private List<T> records;

    public ApiPage(@NotNull final Page<T> page) {
        this(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements(), page.getContent());
    }

    public ApiPage(@NotNull final List<T> records) {
        this(1, records.size(), 1, records.size(), records);
    }
}
