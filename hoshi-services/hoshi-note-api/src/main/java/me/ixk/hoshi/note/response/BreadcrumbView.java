/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Otstar Lin
 * @date 2021/11/25 15:46
 */
@Data
@Builder
@Schema(name = "笔记面包屑")
public class BreadcrumbView {

    private final Item workspace;

    private final List<Item> parent;

    @Data
    @Builder
    public static class Item {

        private final String id;
        private final String name;
    }
}
