package org.h0110w.som.core.configuration.db;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.h0110w.som.core.configuration.CustomProfiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class FlywayConfig {
    @Bean
    @Profile(value = CustomProfiles.DEFAULT_FLYWAY)
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        try {
            flyway.repair();
            flyway.migrate();
        } catch (FlywayException e) {
            log.error("Flyway exception: " + e.getMessage());
        }
        return flyway;
    }

    @Bean
    @Profile(value = CustomProfiles.MOCK_FLYWAY)
    public Flyway mockFlyway(DataSource dataSource) {
        return Flyway.configure().load();
    }
}
