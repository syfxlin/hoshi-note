/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 笔记中心
 *
 * @author Otstar Lin
 * @date 2021/11/17 20:41
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HoshiNoteApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiNoteApplication.class, args);
    }
}
