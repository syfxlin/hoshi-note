package me.ixk.hoshi.common.util;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化工具
 *
 * @author Otstar Lin
 * @date 2021/5/15 下午 3:12
 */
@Component
public class MessageUtil implements MessageSourceAware {

    private static MessageSource messageSource;

    @Override
    public void setMessageSource(final MessageSource m) {
        messageSource = m;
    }

    public static String message(final String key) {
        return message(key, "");
    }

    public static String message(final String key, final String defaultMessage) {
        return message(key, defaultMessage, new Object[0]);
    }

    public static String message(final String key, final String defaultMessage, final Locale locale) {
        return message(key, defaultMessage, locale, new Object[0]);
    }

    public static String message(final String key, final Locale locale) {
        return message(key, null, locale);
    }

    public static String message(final String key, final Object... args) {
        return message(key, "", args);
    }

    public static String message(final String key, final Locale locale, final Object... args) {
        return message(key, "", locale, args);
    }

    public static String message(final String key, final String defaultMessage, final Object... args) {
        final Locale locale = LocaleContextHolder.getLocale();
        return message(key, defaultMessage, locale, args);
    }

    public static String message(
        final String key,
        final String defaultMessage,
        final Locale locale,
        final Object... args
    ) {
        return messageSource.getMessage(key, args, defaultMessage, locale);
    }
}
