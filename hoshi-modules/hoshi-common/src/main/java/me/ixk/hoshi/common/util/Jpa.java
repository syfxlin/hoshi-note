package me.ixk.hoshi.common.util;

import java.beans.PropertyDescriptor;
import me.ixk.hoshi.common.exception.UnsupportedInstantiationException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

/**
 * @author Otstar Lin
 * @date 2021/5/19 下午 4:09
 */
public final class Jpa {

    private Jpa() throws UnsupportedInstantiationException {
        throw new UnsupportedInstantiationException(Jpa.class);
    }

    public static <T> T updateNull(final T update, final T original) {
        Assert.notNull(update, "要更新的元素必须不能为空");
        Assert.notNull(original, "原始元素必须不能为空");
        final BeanWrapper originalWrapper = new BeanWrapperImpl(original);
        final BeanWrapper updateWrapper = new BeanWrapperImpl(update);
        for (final PropertyDescriptor property : originalWrapper.getPropertyDescriptors()) {
            final String name = property.getName();
            final Object updateValue = updateWrapper.getPropertyValue(name);
            if (updateValue == null) {
                final Object originalValue = originalWrapper.getPropertyValue(name);
                updateWrapper.setPropertyValue(name, originalValue);
            }
        }
        return update;
    }

    public static String like(final Object object) {
        return "%" + object + "%";
    }

    public static String leftLike(final Object object) {
        return "%" + object;
    }

    public static String rightLike(final Object object) {
        return object + "%";
    }
}
