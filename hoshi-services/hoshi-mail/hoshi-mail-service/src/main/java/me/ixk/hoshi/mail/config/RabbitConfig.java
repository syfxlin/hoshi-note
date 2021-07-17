package me.ixk.hoshi.mail.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Otstar Lin
 * @date 2021/7/17 17:54
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue emailQueue() {
        return new Queue("email");
    }
}
