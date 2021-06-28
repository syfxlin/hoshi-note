/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums;

import me.ixk.hoshi.client.annotation.EnableHoshiClient;
import me.ixk.hoshi.log.HoshiLogApiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHoshiClient(basePackageClasses = { HoshiLogApiAutoConfiguration.class, HoshiUmsApiAutoConfiguration.class })
public class HoshiUmsApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiUmsApplication.class, args);
    }
}
