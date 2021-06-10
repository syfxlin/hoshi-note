/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.json;

import com.fasterxml.jackson.databind.ser.PropertyFilter;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

/**
 * {@link JsonActive} 过滤器解析器
 *
 * @author Otstar Lin
 * @date 2021/6/1 14:30
 */
@Component
public class JsonActiveResolver extends DynamicFilterResolver<JsonActive> {

    @Override
    public PropertyFilter apply(final JsonActive jsonActive, final MethodParameter methodParameter) {
        return new JsonModelDynamicFilter(jsonActive.value());
    }
}
