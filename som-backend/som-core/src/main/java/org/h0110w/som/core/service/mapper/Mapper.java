package org.h0110w.som.core.service.mapper;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.h0110w.som.core.service.util.auth.KeycloakAuthResponse;
import org.mapstruct.factory.Mappers;

public class Mapper {
    private static Mapper instance;
    private final ObjectMapper objectMapper;

    private ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private Mapper() {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(KeycloakAuthResponseDeserializer.class.getSimpleName(), new Version(1, 0, 0, null, null, null));
        module.addDeserializer(KeycloakAuthResponse.class, new KeycloakAuthResponseDeserializer());
        this.objectMapper.registerModule(module);
    }

    public static ObjectMapper getMapper() {
        if (instance == null) {
            instance = new Mapper();
        }
        return instance.getObjectMapper();
    }

    public static UserAccountMapper USER_ACCOUNT = Mappers.getMapper(UserAccountMapper.class);
    public static UserMapper USER = Mappers.getMapper(UserMapper.class);
    public static TaskMapper TASK = Mappers.getMapper(TaskMapper.class);

}
