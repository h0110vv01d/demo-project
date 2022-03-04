package org.h0110w.som.core.service.mapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.h0110w.som.core.service.util.auth.KeycloakAuthResponse;


import java.io.IOException;

public class KeycloakAuthResponseDeserializer extends StdDeserializer<KeycloakAuthResponse> {
    public KeycloakAuthResponseDeserializer() {
        this(null);
    }

    protected KeycloakAuthResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KeycloakAuthResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        KeycloakAuthResponse token = new KeycloakAuthResponse();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        token.setAccessToken(node.get("access_token").asText());
        token.setScope(node.get("scope").asText());
        token.setTokenType(node.get("token_type").asText());
        token.setRefreshToken(node.get("refresh_expires_in").asText());
        token.setExpiresIn(node.get("expires_in").asInt());
        token.setRefreshExpiresIn(node.get("refresh_expires_in").asInt());
        token.setNotBeforePolicy(node.get("not-before-policy").asInt());
        token.setSessionState(node.get("session_state").asText());

        return token;
    }
}
