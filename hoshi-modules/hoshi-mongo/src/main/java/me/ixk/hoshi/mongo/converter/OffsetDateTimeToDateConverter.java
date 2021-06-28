/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mongo.converter;

import java.time.OffsetDateTime;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;

/**
 * {@link OffsetDateTime} 到 {@link Date} 转换器
 *
 * @author Otstar Lin
 * @date 2021/6/4 22:38
 */
public enum OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {
    INSTANCE;

    @Override
    public Date convert(final OffsetDateTime source) {
        return Date.from(source.toInstant());
    }
}
