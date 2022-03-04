package org.h0110w.somclient.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;

    @Override
    public String toString() {
        return statusCode + ": " + message;
    }
}
