/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.details;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpHeaders;

/**
 * 保留用户登录的信息
 * <p>
 * 保留了用户登录时的 IP，UA 等
 *
 * @author Otstar Lin
 * @date 2021/5/27 22:57
 */
@Data
public class WebAuthenticationDetails implements Serializable {

    private final String address;
    private final String userAgent;

    public WebAuthenticationDetails(final HttpServletRequest request) {
        this.address = request.getRemoteAddr();
        this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
    }
}
