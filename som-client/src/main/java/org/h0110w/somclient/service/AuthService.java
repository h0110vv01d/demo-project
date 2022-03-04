package org.h0110w.somclient.service;

import lombok.extern.slf4j.Slf4j;
import org.h0110w.somclient.exception.ServiceException;
import org.h0110w.somclient.service.dto.AuthRequest;
import org.h0110w.somclient.service.dto.AuthToken;

import java.io.IOException;

@Slf4j
public class AuthService extends AbstractService {
    private static final String TOKEN_URL = "/auth/token";
    private static final String ME_URL = "/auth/me";
    private String role;
    private AuthToken token = null;

    public void authenticate(String login, String password) throws IOException {
        token = client.post(TOKEN_URL, new AuthRequest(login, password),
                AuthToken.class);
        log.info("successful authentication");
        client.setToken(token);
    }

    public boolean isAuthenticated() {
        try {
            role = client.get(ME_URL, String.class);
        } catch (ServiceException | IOException e) {
            log.error("get request " + ME_URL + " failed");
        }
        return role != null;
    }

    public void logout() {
        token = null;
        client.setToken(null);
    }
}
