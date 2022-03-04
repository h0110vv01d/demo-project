package org.h0110w.som.core.configuration.db;

import org.h0110w.som.core.common.AbstractTestWithDbContainer;
import org.h0110w.som.core.configuration.CustomProfiles;
import org.h0110w.som.core.configuration.security.AdminInitializer;
import org.h0110w.som.core.controller.UserAccountsController;
import org.h0110w.som.core.repository.UserAccountsRepository;
import org.h0110w.som.core.service.UserAccountsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles(profiles = {
        CustomProfiles.DEFAULT_DB,
        CustomProfiles.MOCK_FLYWAY,
        CustomProfiles.HIBERNATE_GENERATION})
@MockBeans(value = {
        @MockBean(AdminInitializer.class),
        @MockBean(UserAccountsService.class),
        @MockBean(UserAccountsRepository.class),
        @MockBean(UserAccountsController.class)})
class SchemaGenerationTest
        extends AbstractTestWithDbContainer {
    private static final String SCHEMA_PATH = "src/main/resources/db/schema.sql";

    @Test
    void test() {

    }

//    @DynamicPropertySource
//    static void overrideProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.jpa.properties.javax.persistence.schema-generation.scripts.action", () -> "create");
//        registry.add("spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target", () -> SCHEMA_PATH);
//        registry.add("spring.jpa.properties.javax.persistence.schema-generation.create-source", () -> "metadata");
//
//        registry.add("spring.jpa.properties.hibernate.hbm2ddl.delimiter", () -> ";");
//
//
//        registry.add("spring.jpa.hibernate.hbm2ddl.auto", () -> "create");
//        registry.add("spring.jpa.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
//        registry.add("spring.jpa.hibernate.show_sql", () -> "true");
//        registry.add("spring.jpa.hibernate.format_sql", () -> "true");
//        registry.add("spring.jpa.hibernate.use_sql_comments", () -> "true");
//        registry.add("spring.flyway.enabled", () -> "false");
//    }
}