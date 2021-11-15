/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 将 {@link ApplicationContext} 静态化，便于一些操作使用
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 4:55
 */
@Component
public class App implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull final ApplicationContext applicationContext) throws BeansException {
        synchronized (this) {
            if (App.applicationContext == null) {
                App.applicationContext = applicationContext;
            }
        }
    }

    public static ApplicationContext get() {
        return applicationContext;
    }

    public static <T> T getBean(final Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(final String qualifier, final Class<T> clazz) {
        return applicationContext.getBean(qualifier, clazz);
    }
}
