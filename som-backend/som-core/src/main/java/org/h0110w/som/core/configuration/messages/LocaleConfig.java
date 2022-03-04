package org.h0110w.som.core.configuration.messages;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class LocaleConfig {
    @Bean
    public Locale enLocale() {
        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        return locale;
    }

//    @Bean
//    public Locale ruLocale() {
//        Locale locale = new Locale("ru", "RU");
//        Locale.setDefault(locale);
//        return locale;
//    }
}