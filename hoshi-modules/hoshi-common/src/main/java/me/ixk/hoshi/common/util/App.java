/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 4:55
 */
@Component
public class App implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
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
