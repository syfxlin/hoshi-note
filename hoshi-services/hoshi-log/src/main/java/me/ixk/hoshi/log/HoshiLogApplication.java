/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Otstar Lin
 * @date 2021/6/4 20:24
 */
@SpringBootApplication
@EnableMongoRepositories
public class HoshiLogApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiLogApplication.class, args);
    }
}
