/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.util.Objects;

/**
 * 动态过滤器的提供者
 *
 * @author Otstar Lin
 * @date 2021/6/1 13:46
 */
public class DynamicFilterProvider extends SimpleFilterProvider implements PropertyFilter {

    public static final String FILTER_NAME = "DynamicFilterProvider$FILTER";

    /**
     * 委托字段过滤器
     */
    private final PropertyFilter delegate;

    public DynamicFilterProvider() {
        // 默认包含全部字段
        this(SimpleBeanPropertyFilter.serializeAll());
    }

    public DynamicFilterProvider(final PropertyFilter delegate) {
        this.delegate = Objects.requireNonNull(delegate);
        this.addFilter(FILTER_NAME, this);
    }

    @Override
    public void serializeAsField(
        final Object pojo,
        final JsonGenerator gen,
        final SerializerProvider prov,
        final PropertyWriter writer
    ) throws Exception {
        this.delegate.serializeAsField(pojo, gen, prov, writer);
    }

    @Override
    public void serializeAsElement(
        final Object elementValue,
        final JsonGenerator gen,
        final SerializerProvider prov,
        final PropertyWriter writer
    ) throws Exception {
        this.delegate.serializeAsElement(elementValue, gen, prov, writer);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void depositSchemaProperty(
        final PropertyWriter writer,
        final ObjectNode propertiesNode,
        final SerializerProvider provider
    ) throws JsonMappingException {
        this.delegate.depositSchemaProperty(writer, propertiesNode, provider);
    }

    @Override
    public void depositSchemaProperty(
        final PropertyWriter writer,
        final JsonObjectFormatVisitor objectVisitor,
        final SerializerProvider provider
    ) throws JsonMappingException {
        this.delegate.depositSchemaProperty(writer, objectVisitor, provider);
    }
}
