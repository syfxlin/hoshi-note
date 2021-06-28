/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mongo.config;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import me.ixk.hoshi.mongo.converter.DateToOffsetDateTimeConverter;
import me.ixk.hoshi.mongo.converter.OffsetDateTimeToDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/**
 * 默认 Mongo 设置
 *
 * @author Otstar Lin
 * @date 2021/6/4 22:34
 */
@Configuration
public class DefaultMongoConfig {

    /**
     * 添加 {@link OffsetDateTime} 与 {@link Date} 的转换器
     *
     * @return Mongo 自定义转换器
     */
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
            Arrays.asList(DateToOffsetDateTimeConverter.INSTANCE, OffsetDateTimeToDateConverter.INSTANCE)
        );
    }
}
