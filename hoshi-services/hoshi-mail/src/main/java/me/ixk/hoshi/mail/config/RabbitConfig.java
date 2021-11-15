/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mail.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 *
 * @author Otstar Lin
 * @date 2021/11/13 22:31
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue emailQueue() {
        return new Queue("email");
    }
}
