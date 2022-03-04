package org.h0110w.somclient.utils.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomObjectMapper {
    private static CustomObjectMapper instance;
    private final ObjectMapper objectMapper;

    private ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private CustomObjectMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public static ObjectMapper getMapper() {
        if (instance == null) {
            instance = new CustomObjectMapper();
        }
        return instance.getObjectMapper();
    }

}
