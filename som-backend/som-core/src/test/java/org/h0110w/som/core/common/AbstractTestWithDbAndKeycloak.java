package org.h0110w.som.core.common;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

//@ExtendWith(KeycloakInitializerExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractTestWithDbAndKeycloak extends AbstractTestWithDbContainer {
    static final KeycloakContainer keycloakContainer;

    static {
        keycloakContainer = new KeycloakContainer("jboss/keycloak:15.0.1")
                .withRealmImportFile("/test_realm-export.json")
                .withAdminUsername("admin")
                .withAdminPassword(TestsConfig.TEST_PASSWORD)
                .withReuse(true);
        keycloakContainer.start();
    }


    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("keycloak-admin.clientSecret", () -> TestsConfig.TEST_SECRET_SPRING_BOOT_ADMIN);
        registry.add("keycloak-admin.url", keycloakContainer::getAuthServerUrl);
        registry.add("keycloak.auth-server-url", keycloakContainer::getAuthServerUrl);
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> keycloakContainer.getAuthServerUrl() +
                        "/realms/som_realm/protocol/openid-connect/certs");
    }
}
