package org.h0110w.som.core.common.test_utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.h0110w.som.core.SomApplication;
import org.h0110w.som.core.configuration.security.keycloak.KeycloakConfig;
import org.h0110w.som.core.service.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;

import static org.h0110w.som.core.common.TestsConfig.TEST_SECRET_SPRING_BOOT_ADMIN;
import static org.h0110w.som.core.common.TestsConfig.TEST_SECRET_USERS;

//@Component
public class RealmSettingsConfigurer {
    private static final String CLIENTS_FIELD_NAME = "clients";
    private static final String CLIENT_ID_FIELD_NAME = "clientId";
    private static final String SECRET_FIELD_NAME = "secret";
    private static final String REALM_JSON_SETTINGS_PATH = "/realm-export.json";
    @Autowired
    private KeycloakConfig keycloakConfig;


    public String getTestRealmSettings() throws IOException {
        JsonNode base = getBaseJsonNode();
        base.get(CLIENTS_FIELD_NAME)
                .forEach(node -> {
                    String clientId = node.get(CLIENT_ID_FIELD_NAME).asText();
                    final boolean clientIdIsAdmin = clientId.equals(keycloakConfig.getSpringBootAdminClientId());
                    final boolean clientIdIsUsers = clientId.equals(keycloakConfig.getUsersClientId());
                    if (clientIdIsAdmin) {
                        ((ObjectNode) node).put(SECRET_FIELD_NAME, TEST_SECRET_SPRING_BOOT_ADMIN);
                    } else if (clientIdIsUsers) {
                        ((ObjectNode) node).put(SECRET_FIELD_NAME, TEST_SECRET_USERS);
                    }
                });


        return base.asText();
    }

    private static JsonNode getBaseJsonNode() throws IOException {
        URL res = SomApplication.class.getResource(REALM_JSON_SETTINGS_PATH);
        return Mapper.getMapper().readTree(res);
    }
}
