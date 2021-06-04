/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mongo.config;

import java.util.Arrays;
import me.ixk.hoshi.mongo.converter.DateToOffsetDateTimeConverter;
import me.ixk.hoshi.mongo.converter.OffsetDateTimeToDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/**
 * @author Otstar Lin
 * @date 2021/6/4 22:34
 */
@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
            Arrays.asList(DateToOffsetDateTimeConverter.INSTANCE, OffsetDateTimeToDateConverter.INSTANCE)
        );
    }
}
