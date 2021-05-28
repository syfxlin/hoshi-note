/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 2:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页")
public class PageView {

    @ApiModelProperty("当前页（为 null 则不分页）")
    private Integer page;

    @ApiModelProperty("页大小")
    private int pageSize = 15;

    @ApiModelProperty("排序规则（默认 ASC，通过 : 分割排序的列和排序方向）")
    private List<String> orderBy = new ArrayList<>();

    public Pageable toPage() {
        final Sort sort = Sort.by(
            this.orderBy.stream()
                .map(
                    o -> {
                        final String[] split = o.split(":");
                        if (split.length == 1 || "ASC".equalsIgnoreCase(split[1])) {
                            return Sort.Order.asc(split[0]);
                        } else {
                            return Sort.Order.desc(split[1]);
                        }
                    }
                )
                .collect(Collectors.toList())
        );
        return PageRequest.of(this.page, this.pageSize, sort);
    }
}
