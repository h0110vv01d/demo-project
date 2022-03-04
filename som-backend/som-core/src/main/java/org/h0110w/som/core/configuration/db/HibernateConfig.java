package org.h0110w.som.core.configuration.db;

import org.h0110w.som.core.configuration.CustomProfiles;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = CustomProfiles.HIBERNATE_GENERATION)
public class HibernateConfig {

}
