/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 监控中心
 *
 * @author Otstar Lin
 * @date 2021/6/24 19:37
 */
@SpringBootApplication
@EnableAdminServer
public class HoshiAdminApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiAdminApplication.class, args);
    }
}
