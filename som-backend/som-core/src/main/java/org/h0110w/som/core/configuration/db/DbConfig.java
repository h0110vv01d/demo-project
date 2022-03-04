package org.h0110w.som.core.configuration.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.h0110w.som.core.configuration.CustomProfiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "org.h0110w.som.core.repository")
@EnableTransactionManagement
@Getter
@Profile(value = CustomProfiles.DEFAULT_DB)
public class DbConfig {
    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.port}")
    private String DB_PORT;

    @Value("${spring.datasource.name}")
    private String DB_NAME;

    @Value("${spring.datasource.user}")
    private String DB_LOGIN;

    @Value("${spring.datasource.pass}")
    private String DB_PASS;

    @Bean
    public DataSource dataSource() {
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.serverName", DB_URL);
        props.setProperty("dataSource.portNumber", DB_PORT);
        props.setProperty("dataSource.databaseName", DB_NAME);


        HikariConfig config = new HikariConfig(props);
        config.setUsername(DB_LOGIN);
        config.setPassword(DB_PASS);

        return new HikariDataSource(config);
    }
}