/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mongo.converter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Otstar Lin
 * @date 2021/6/4 22:38
 */
public enum DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {
    INSTANCE;

    @Override
    public OffsetDateTime convert(@NotNull final Date source) {
        return OffsetDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }
}
