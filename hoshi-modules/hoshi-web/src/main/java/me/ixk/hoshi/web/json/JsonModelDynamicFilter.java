/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.json;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import java.util.Arrays;
import me.ixk.hoshi.web.json.JsonMode.Include;

/**
 * {@link JsonMode} 动态过滤器
 *
 * @author Otstar Lin
 * @date 2021/6/1 15:22
 */
public class JsonModelDynamicFilter extends SimpleBeanPropertyFilter {

    /**
     * 激活的过滤器模式
     */
    private final Class<?> active;
    /**
     * 是否是排除模式，默认为排除模式
     */
    private final boolean exclude;

    public JsonModelDynamicFilter(final Class<?> active) {
        this.active = active;
        // 当模式类上标注 Include 注解则设置为非排除模式
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
            // 排除模式下，所有设置的模式都必须不能为当前激活模式或子类，否则就过滤掉该字段
            return Arrays.stream(mode.value()).noneMatch(clazz -> clazz.isAssignableFrom(this.active));
        } else {
            // 包含模式下，需要是设置的模式的其中一个的子类，否则就过滤掉该字段
            return Arrays.stream(mode.value()).anyMatch(clazz -> clazz.isAssignableFrom(this.active));
        }
    }
}
