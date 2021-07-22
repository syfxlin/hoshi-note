package me.ixk.hoshi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Otstar Lin
 * @date 2021/7/22 11:11
 */
@Configuration
@EnableTransactionManagement
public class DefaultRedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }
}
