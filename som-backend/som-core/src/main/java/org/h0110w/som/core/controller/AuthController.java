package org.h0110w.som.core.controller;

import org.h0110w.som.core.controller.request.AuthRequest;
import org.h0110w.som.core.service.CustomSecurityContext;
import org.h0110w.som.core.model.user_account.UserAccountDto;
import org.h0110w.som.core.service.KeycloakAuthClient;
import org.h0110w.som.core.service.mapper.Mapper;
import org.h0110w.som.core.service.util.auth.KeycloakAuthResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final KeycloakAuthClient authClient;
    private final CustomSecurityContext customSecurityContext;

    public AuthController(KeycloakAuthClient authClient, CustomSecurityContext customSecurityContext) {
        this.authClient = authClient;
        this.customSecurityContext = customSecurityContext;
    }

    @PostMapping("/token")
    public KeycloakAuthResponse auth(@Valid @RequestBody AuthRequest request) {
        return authClient.getToken(request);
    }

    //todo return role from principal
    @GetMapping("/my-account")
    public UserAccountDto myAccount() {
        return Mapper.USER_ACCOUNT.toDto(customSecurityContext.getCurrentUser());
    }
}
