package me.ixk.hoshi.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 自动配置
 *
 * @author Otstar Lin
 * @date 2021/5/20 20:15
 */
@Configuration
@ComponentScan("me.ixk.hoshi.db")
@EnableJpaRepositories("me.ixk.hoshi.db")
@EntityScan("me.ixk.hoshi.db")
public class HoshiUserDbConfiguration {}
