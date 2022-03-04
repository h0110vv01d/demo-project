package org.h0110w.som.core.configuration.messages;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Executable;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Wrapper class over standard MessageSource for better usability
 */
@Service
public class CustomMessageSource {
    private final MessageSource messageSource;
    private final Locale locale;

    public CustomMessageSource(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public String get(@NotNull String code) {
        return tryToGetMessage(code, () -> messageSource.getMessage(code, null, locale));
    }

    public String get(@NotNull String code, @Nullable Object arg) {
        return tryToGetMessage(code, () -> messageSource.getMessage(code, new Object[]{arg}, locale));
    }

    public String get(@NotNull String code, @Nullable Object arg0, @Nullable Object arg1) {
        return tryToGetMessage(code, () -> messageSource.getMessage(code, new Object[]{arg0, arg1}, locale));
    }

    public String get(@NotNull String code, @Nullable Object arg0, @Nullable Object arg1, @Nullable Object arg2) {
        return tryToGetMessage(code, () -> messageSource.getMessage(code, new Object[]{arg0, arg1, arg2}, locale));
    }

    private String tryToGetMessage(String code, Supplier<String> supplier) {
        try {
            return supplier.get();
        } catch (NoSuchMessageException e) {
            return code;
        }
    }
}

