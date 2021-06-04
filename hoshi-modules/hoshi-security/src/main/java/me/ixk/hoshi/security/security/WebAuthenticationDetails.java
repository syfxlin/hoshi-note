/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.security;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * @author Otstar Lin
 * @date 2021/5/27 22:57
 */
@Data
public class WebAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String address;
    private final String userAgent;

    public WebAuthenticationDetails(final HttpServletRequest request) {
        this.address = request.getRemoteAddr();
        this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
    }
}
