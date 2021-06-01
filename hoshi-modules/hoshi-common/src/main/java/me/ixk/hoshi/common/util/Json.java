/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.ixk.hoshi.common.exception.JsonException;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 11:34
 */
public final class Json extends ObjectMapper {

    private static final long serialVersionUID = 8349185508138474950L;

    private Json() {
        super();
        this.registerModule(new JavaTimeModule());
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }

    public static Json make() {
        return Inner.INSTANCE;
    }

    private static class Inner {

        private static final Json INSTANCE = new Json();
    }

    public static ObjectNode parseObject(final String json) {
        try {
            return make().readValue(json, ObjectNode.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static ArrayNode parseArray(final String json) {
        try {
            return make().readValue(json, ArrayNode.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static JsonNode parse(final String json) {
        try {
            return make().readTree(json);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T parse(final String json, final Class<T> clazz) {
        try {
            return make().readValue(json, clazz);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static String toJson(final Object object) {
        try {
            return make().writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static String toJson(final JsonNode node) {
        return node.toString();
    }

    public static ObjectNode createObject() {
        return make().createObjectNode();
    }

    public static ArrayNode createArray() {
        return make().createArrayNode();
    }

    public static JsonNode convertToNode(final Object object) {
        return make().valueToTree(object);
    }

    public static ObjectNode convertToObjectNode(final Object object) {
        return (ObjectNode) convertToNode(object);
    }

    public static ArrayNode convertToArrayNode(final Object object) {
        return (ArrayNode) convertToNode(object);
    }

    public static <T> T convertToObject(final JsonNode node, final Class<T> type) {
        try {
            return make().treeToValue(node, type);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }
}
