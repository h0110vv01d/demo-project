package org.h0110w.som.core.common;

import org.h0110w.som.core.common.test_utils.TestConfig;
import org.h0110w.som.core.configuration.CustomProfiles;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
//.testcontainers.properties
//docker.client.strategy=org.testcontainers.dockerclient.UnixSocketClientProviderStrategy
//testcontainers.reuse.enable=true

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = {
        CustomProfiles.DEFAULT_DB,
        CustomProfiles.DEFAULT_FLYWAY})
@Import(TestConfig.class)
public class AbstractTestWithDbContainer {
    protected static final GenericContainer genericPostgreSQLContainer;
    protected static final PostgreSQLContainer postgreSQLContainer;
    static {
        genericPostgreSQLContainer = new PostgreSQLContainer("postgres:12-alpine")
                .withDatabaseName("som_core_db")
                .withUsername("postgres")
                .withPassword("123qwe")
                .withReuse(true);
        postgreSQLContainer = (PostgreSQLContainer) genericPostgreSQLContainer;
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getHost);
        registry.add("spring.datasource.port", postgreSQLContainer::getFirstMappedPort);
        registry.add("spring.datasource.name", postgreSQLContainer::getDatabaseName);
        registry.add("spring.datasource.user", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.pass", postgreSQLContainer::getPassword);
    }
}
