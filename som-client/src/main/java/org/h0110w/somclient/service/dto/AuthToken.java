package org.h0110w.somclient.service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {
    private String accessToken;
    private int expiresIn;
    private int refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private int notBeforePolicy;
    private String sessionState;
    private String scope;
}
