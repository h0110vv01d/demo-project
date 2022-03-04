package org.h0110w.somclient.service.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String clientId = "som_core_users";
    private String clientSecret = "17d76b65-b8dc-44e6-b23a-73813a2feb63";

    public AuthRequest(String login, String password) {
        this.username = login;
        this.password = password;
    }
}
