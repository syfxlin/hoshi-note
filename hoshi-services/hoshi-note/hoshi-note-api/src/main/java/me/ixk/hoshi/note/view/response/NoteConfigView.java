/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/6/6 11:33
 */
@Data
@Builder
public class NoteConfigView {

    private final Long id;
    private final String password;
}
