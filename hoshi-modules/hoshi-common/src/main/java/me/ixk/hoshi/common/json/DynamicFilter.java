/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.json;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import java.util.Arrays;
import me.ixk.hoshi.common.json.JsonMode.Include;

/**
 * @author Otstar Lin
 * @date 2021/6/1 15:22
 */
public class DynamicFilter extends SimpleBeanPropertyFilter {

    private final Class<?> active;
    private final boolean exclude;

    public DynamicFilter(final Class<?> active) {
        this.active = active;
        this.exclude = this.active.getAnnotation(Include.class) == null;
    }

    @Override
    protected boolean include(final BeanPropertyWriter writer) {
        return this.include((PropertyWriter) writer);
    }

    @Override
    protected boolean include(final PropertyWriter writer) {
        final JsonMode mode = writer.getAnnotation(JsonMode.class);
        if (mode == null) {
            return this.exclude;
        }
        if (this.exclude) {
            return Arrays.stream(mode.value()).noneMatch(clazz -> clazz.isAssignableFrom(this.active));
        } else {
            return Arrays.stream(mode.value()).anyMatch(clazz -> clazz.isAssignableFrom(this.active));
        }
    }
}
