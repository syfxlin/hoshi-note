/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view.response;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import me.ixk.hoshi.common.json.JsonMode;

/**
 * @author Otstar Lin
 * @date 2021/5/30 20:54
 */
@Data
@Builder
public class NoteView {

    private final String id;
    private final String parent;
    private final List<String> children;
    private final String workspace;
    private final String name;

    @JsonMode(NoContent.class)
    private final String content;

    private final Long version;
    private final String type;
    private final Integer status;
    private final OffsetDateTime createdTime;
    private final OffsetDateTime updatedTime;
    private final NoteConfigView config;

    public static class NoContent {}
}
