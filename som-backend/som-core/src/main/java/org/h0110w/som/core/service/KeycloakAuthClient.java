package org.h0110w.som.core.service;

import lombok.extern.slf4j.Slf4j;
import org.h0110w.som.core.controller.request.AuthRequest;
import org.h0110w.som.core.exception.AuthException;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.service.mapper.Mapper;
import org.h0110w.som.core.service.util.auth.KeycloakAuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

@Component
@Slf4j
public class KeycloakAuthClient {
    private final String tokenUrl;
    private final WebClient client;

    public KeycloakAuthClient(@Value("${keycloak-admin.realm}") String realm,
                              @Value("${keycloak-admin.url}") String keycloakURL) {

        this.tokenUrl = keycloakURL + "/realms/" + realm + "/protocol/openid-connect/token";
        this.client = WebClient.create(keycloakURL);
    }

    public KeycloakAuthResponse getToken(AuthRequest authRequest) {
        String response = performRequest(authRequest);
        return parseResponse(response);
    }

    private KeycloakAuthResponse parseResponse(String response) {
        KeycloakAuthResponse token = null;
        try {
            token = Mapper.getMapper().readValue(new InputStreamReader(
                            new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8))),
                    KeycloakAuthResponse.class);
        } catch (IOException ex) {
            throwAuthException();
        }
        return token;
    }

    private String performRequest(AuthRequest request) {
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(tokenUrl);
        WebClient.RequestHeadersSpec headersSpec = bodySpec.body(
                BodyInserters.fromFormData("username", request.getUsername())
                        .with("password", request.getPassword())
                        .with("client_id", request.getClientId())
                        .with("client_secret", request.getClientSecret())
                        .with("grant_type", "password"));
        WebClient.ResponseSpec responseSpec = headersSpec.header(
                        HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve();

        return responseSpec
                .onStatus(status -> status.value() == HttpStatus.BAD_REQUEST.value(),
                        response -> Mono.error(new ServiceException("Bad auth request")))
                .onStatus(status -> status.value() == HttpStatus.UNAUTHORIZED.value(),
                        response -> Mono.error(new AuthException("Authentication failed")))
                .bodyToMono(String.class)
                .block();
    }

    private void throwAuthException() {
        throw new AuthException("Invalid token");
    }

}
