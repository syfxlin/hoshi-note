/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mysql.util;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

/**
 * JPA 工具类
 *
 * @author Otstar Lin
 * @date 2021/6/5 13:57
 */
public final class Jpa {

    private Jpa() {}

    public static String[] getNullPropertyNames(final Object instance) {
        final BeanWrapper src = new BeanWrapperImpl(instance);
        return Arrays
            .stream(src.getPropertyDescriptors())
            .map(PropertyDescriptor::getName)
            .filter(name -> src.getPropertyValue(name) == null)
            .toArray(String[]::new);
    }

    public static String[] getNotNullPropertyNames(final Object instance) {
        final BeanWrapper src = new BeanWrapperImpl(instance);
        return Arrays
            .stream(src.getPropertyDescriptors())
            .map(PropertyDescriptor::getName)
            .filter(name -> src.getPropertyValue(name) != null)
            .toArray(String[]::new);
    }

    /**
     * 合并 JPA Entity，update 的字段不为空说明有更新，有更新的字段会设置到 target 上
     *
     * @param update 更新来源对象，不为空表示有更新
     * @param target 原始对象，旧的 Entity 对象，要被更新的对象
     * @param <T>    类型
     * @return 更新后的对象
     */
    public static <T> T merge(final T update, final T target) {
        Assert.notNull(update, "原始元素必须不能为空");
        Assert.notNull(target, "要更新的元素必须不能为空");
        BeanUtils.copyProperties(update, target, getNullPropertyNames(update));
        return target;
    }
}
