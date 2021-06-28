/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file;

import me.ixk.hoshi.client.annotation.EnableHoshiClient;
import me.ixk.hoshi.ums.HoshiUmsApiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Otstar Lin
 * @date 2021/5/23 9:04
 */
@EnableHoshiClient(basePackageClasses = { HoshiUmsApiAutoConfiguration.class })
@SpringBootApplication
public class HoshiFileApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiFileApplication.class, args);
    }
}
