package me.ixk.hoshi.mail;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author Otstar Lin
 * @date 2021/7/17 16:20
 */
@Configuration
@ComponentScan
@EnableRedisRepositories
public class HoshiMailApiAutoConfiguration {}
