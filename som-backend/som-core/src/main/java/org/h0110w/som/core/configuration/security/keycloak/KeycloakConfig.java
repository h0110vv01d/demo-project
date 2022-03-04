package org.h0110w.som.core.configuration.security.keycloak;

import lombok.Getter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Configuration
@Getter
public class KeycloakConfig {
    private static Keycloak client = null;
    @Value("${keycloak-admin.url}")
    private String keycloakUrl;

    @Value("${keycloak-admin.realm}")
    private String realm;

    @Value("${keycloak-admin.clientId}")
    private String springBootAdminClientId;

    @Value("${keycloak-admin.clientSecret}")
    private String clientSecret;

    @Value("${spring.security.admin_username}")
    private String somRealmAdminUsername;

    @Value("${keycloak-admin.admin_password}")
    private String adminPassword;

    @Value("${keycloak-admin.users_clientId}")
    private String usersClientId;



    @Bean
    public Keycloak keycloakAdminClient() {
        if (client == null) {
            client = KeycloakBuilder.builder()
                    .serverUrl(keycloakUrl)
                    .realm(realm)
                    .grantType(CLIENT_CREDENTIALS)
                    .clientId(springBootAdminClientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        }
        return client;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String getTokenPrefix() {
        return "Bearer ";
    }
}
