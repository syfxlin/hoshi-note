package me.ixk.hoshi.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.convert.ConversionService;

/**
 * @author Otstar Lin
 * @date 2021/5/22 17:43
 */
public class Data {

    public static JsonNode dataGet(final JsonNode target, final String key) {
        return dataGet(target, key, NullNode.getInstance());
    }

    public static JsonNode dataGet(final JsonNode target, final String key, final JsonNode _default) {
        final String[] keys = key.split("\\.");
        return dataGet(target, keys, _default);
    }

    public static JsonNode dataGet(final JsonNode target, final String[] keys) {
        return dataGet(target, keys, NullNode.getInstance());
    }

    public static JsonNode dataGet(JsonNode target, final String[] keys, final JsonNode _default) {
        if (keys == null) {
            return target;
        }
        for (int i = 0; i < keys.length; i++) {
            if ("*".equals(keys[i])) {
                final ArrayNode array = Json.createArray();
                final String[] subKeys = Arrays.copyOfRange(keys, i + 1, keys.length);
                if (target.isObject()) {
                    final ObjectNode object = (ObjectNode) target;
                    for (final Iterator<String> it = object.fieldNames(); it.hasNext();) {
                        final String itemKey = it.next();
                        final JsonNode element = dataGet(object.get(itemKey), subKeys, null, JsonNode.class);
                        if (element != null) {
                            array.add(element);
                        }
                    }
                } else if (target.isArray()) {
                    final ArrayNode array1 = (ArrayNode) target;
                    for (final JsonNode item : array1) {
                        final JsonNode element = dataGet(item, subKeys, null, JsonNode.class);
                        if (element != null) {
                            array.add(element);
                        }
                    }
                }
                return array;
            } else if (target.isArray()) {
                final ArrayNode array = (ArrayNode) target;
                final int index = Integer.parseInt(keys[i]);
                if (array.size() <= index) {
                    target = null;
                    break;
                }
                target = array.get(index);
            } else if (target.isObject()) {
                final ObjectNode object = (ObjectNode) target;
                if (!object.has(keys[i])) {
                    target = null;
                    break;
                }
                target = object.get(keys[i]);
            } else {
                target = null;
                break;
            }
        }
        if (target == null) {
            return _default;
        }
        return target;
    }

    public static Object dataGet(final Object target, final String key) {
        return dataGet(target, key, null);
    }

    public static Object dataGet(final Object target, final String key, final Object _default) {
        final String[] keys = key.split("\\.");
        return dataGet(target, keys, _default, Object.class);
    }

    public static Object dataGet(final Object target, final String[] keys) {
        return dataGet(target, keys, null, Object.class);
    }

    public static <T> T dataGet(final Object target, final String key, final Class<T> returnType) {
        return dataGet(target, key, null, returnType);
    }

    public static <T> T dataGet(
        final Object target,
        final String key,
        final T defaultValue,
        final Class<T> returnType
    ) {
        final String[] keys = key.split("\\.");
        return dataGet(target, keys, defaultValue, returnType);
    }

    public static <T> T dataGet(final Object target, final String[] keys, final Class<T> returnType) {
        return dataGet(target, keys, null, returnType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T dataGet(Object target, final String[] keys, final T defaultValue, final Class<T> returnType) {
        if (keys == null) {
            return App.getBean(ConversionService.class).convert(target, returnType);
        }
        for (int i = 0; i < keys.length; i++) {
            if ("*".equals(keys[i])) {
                final List<Object> array = new ArrayList<>();
                final String[] subKeys = Arrays.copyOfRange(keys, i + 1, keys.length);
                if (target instanceof Map) {
                    final Map<String, Object> object = (Map<String, Object>) target;
                    for (final String itemKey : object.keySet()) {
                        final Object element = dataGet(object.get(itemKey), subKeys, null, returnType);
                        if (element != null) {
                            array.add(element);
                        }
                    }
                } else if (target instanceof List) {
                    final List<Object> array1 = (List<Object>) target;
                    for (final Object item : array1) {
                        final Object element = dataGet(item, subKeys, null, returnType);
                        if (element != null) {
                            array.add(element);
                        }
                    }
                }
                return App.getBean(ConversionService.class).convert(array, returnType);
            } else if (target instanceof List) {
                final List<Object> array = (List<Object>) target;
                final int index = Integer.parseInt(keys[i]);
                if (array.size() <= index) {
                    target = null;
                    break;
                }
                target = array.get(index);
            } else if (target instanceof Map) {
                final Map<String, Object> object = (Map<String, Object>) target;
                if (!object.containsKey(keys[i])) {
                    target = null;
                    break;
                }
                target = object.get(keys[i]);
            } else {
                target = null;
                break;
            }
        }
        if (target == null) {
            return defaultValue;
        }
        return App.getBean(ConversionService.class).convert(target, returnType);
    }

    public static void dataSet(final JsonNode target, final String key, final JsonNode value) {
        final String[] keys = key.split("\\.");
        dataSet(target, keys, value);
    }

    public static void dataSet(JsonNode target, final String[] keys, final JsonNode value) {
        if (target == null) {
            return;
        }
        for (int i = 0; i < keys.length; i++) {
            if (i != keys.length - 1) {
                if (target.isObject()) {
                    final ObjectNode node = (ObjectNode) target;
                    target = node.get(keys[i]);
                    if (target == null) {
                        target = Json.createObject();
                        node.set(keys[i], target);
                    }
                } else if (target.isArray()) {
                    final ArrayNode node = (ArrayNode) target;
                    final int index = Integer.parseInt(keys[i]);
                    target = node.get(index);
                    if (target == null) {
                        target = Json.createObject();
                        while (node.size() < index) {
                            node.addNull();
                        }
                        node.insert(index, target);
                    }
                } else {
                    throw new ClassCastException("Can not set value to ValueNode");
                }
            } else {
                if (target.isObject()) {
                    final ObjectNode object = (ObjectNode) target;
                    object.set(keys[i], value);
                } else if (target.isArray()) {
                    final ArrayNode array = (ArrayNode) target;
                    final int index = Integer.parseInt(keys[i]);
                    while (array.size() < index) {
                        array.addNull();
                    }
                    array.insert(index, value);
                } else {
                    throw new ClassCastException("Can not set value to ValueNode");
                }
            }
        }
    }

    public static void dataSet(final Object target, final String key, final Object value) {
        final String[] keys = key.split("\\.");
        dataSet(target, keys, value);
    }

    @SuppressWarnings("unchecked")
    public static void dataSet(Object target, final String[] keys, final Object value) {
        if (target == null) {
            return;
        }
        for (int i = 0; i < keys.length; i++) {
            if (i != keys.length - 1) {
                if (target instanceof List) {
                    final List<Object> list = (List<Object>) target;
                    final int index = Integer.parseInt(keys[i]);
                    if (list.size() > index) {
                        target = list.get(index);
                    } else {
                        target = new ConcurrentHashMap<String, Object>();
                        while (list.size() < index) {
                            list.add(null);
                        }
                        list.add(index, target);
                    }
                } else if (target instanceof Map) {
                    final Map<String, Object> map = (Map<String, Object>) target;
                    target = map.get(keys[i]);
                    if (target == null) {
                        target = new ConcurrentHashMap<>();
                        map.put(keys[i], target);
                    }
                } else {
                    throw new ClassCastException("Can not set value to " + target.getClass().getName());
                }
            } else {
                if (target instanceof List) {
                    final List<Object> list = (List<Object>) target;
                    final int index = Integer.parseInt(keys[i]);
                    while (list.size() < index) {
                        list.add(null);
                    }
                    list.add(index, value);
                } else if (target instanceof Map) {
                    final Map<String, Object> map = (Map<String, Object>) target;
                    map.put(keys[i], value);
                } else {
                    throw new ClassCastException("Can not set value to " + target.getClass().getName());
                }
            }
        }
    }
}
