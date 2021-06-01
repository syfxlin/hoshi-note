/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.core.MethodParameter;

/**
 * @author Otstar Lin
 * @date 2021/6/1 13:53
 */
public abstract class DynamicFilterResolver<A extends Annotation>
    extends TypeReference<A>
    implements BiFunction<A, MethodParameter, PropertyFilter> {

    @SuppressWarnings("unchecked")
    public final PropertyFilter resolve(final MethodParameter methodParameter) {
        return Optional
            .ofNullable(methodParameter.getMethodAnnotation((Class<A>) this.getType()))
            .map(a -> this.apply(a, methodParameter))
            .orElse(null);
    }
}
