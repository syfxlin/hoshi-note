/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/5/27 23:08
 */
@Data
@Builder
public class LoggedView {

    private final String sessionId;
    private final String address;
    private final String userAgent;
    private final OffsetDateTime creationTime;
    private final OffsetDateTime lastAccessedTime;
}
