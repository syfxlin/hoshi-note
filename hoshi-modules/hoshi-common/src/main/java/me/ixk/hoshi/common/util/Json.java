/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.ixk.hoshi.common.exception.JsonException;
import me.ixk.hoshi.common.json.DynamicFilter;
import me.ixk.hoshi.common.json.DynamicFilterProvider;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 11:34
 */
@Component
public class Json {

    private static ObjectMapper mapper;

    public Json(final ObjectMapper m) {
        mapper = m;
    }

    public static ObjectMapper get() {
        return mapper;
    }

    public static ObjectNode parseObject(final String json) {
        try {
            return mapper.readValue(json, ObjectNode.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static ArrayNode parseArray(final String json) {
        try {
            return mapper.readValue(json, ArrayNode.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static JsonNode parse(final String json) {
        try {
            return mapper.readTree(json);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T parse(final String json, final Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static String toJson(final Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static String toJson(final JsonNode node) {
        return node.toString();
    }

    public static String toJson(final Object object, final Class<?> active) {
        try {
            return mapper.writer(new DynamicFilterProvider(new DynamicFilter(active))).writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static ObjectNode createObject() {
        return mapper.createObjectNode();
    }

    public static ArrayNode createArray() {
        return mapper.createArrayNode();
    }

    public static JsonNode convertToNode(final Object object) {
        return mapper.valueToTree(object);
    }

    public static ObjectNode convertToObjectNode(final Object object) {
        return (ObjectNode) convertToNode(object);
    }

    public static ArrayNode convertToArrayNode(final Object object) {
        return (ArrayNode) convertToNode(object);
    }

    public static <T> T convertToObject(final JsonNode node, final Class<T> type) {
        try {
            return mapper.treeToValue(node, type);
        } catch (final JsonProcessingException e) {
            throw new JsonException(e);
        }
    }
}
