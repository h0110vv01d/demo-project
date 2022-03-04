package org.h0110w.som.core.common.test_utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestBodyConstructor {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
