/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.view.request.log;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/6/4 20:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddLogView {

    private String type;
    private String method;
    private String ip;
    private String user;
    private String module;
    private String operate;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Boolean status;
    private String message;
}
