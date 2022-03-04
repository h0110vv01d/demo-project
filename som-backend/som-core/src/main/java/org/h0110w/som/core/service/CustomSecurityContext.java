package org.h0110w.som.core.service;


import org.h0110w.som.core.model.user_account.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomSecurityContext {
    private final UserAccountsService userAccountsService;

    @Autowired
    public CustomSecurityContext(UserAccountsService userAccountsService) {
        this.userAccountsService = userAccountsService;
    }

    public Authentication getCurrentPrincipal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return null;
        }
        return securityContext.getAuthentication();
    }


    public UserAccount getCurrentUser() {
        Authentication authentication = getCurrentPrincipal();
        if (authentication == null) {
            return null;
        }
        return userAccountsService.getById(UUID.fromString(authentication.getName()));
    }

    public UUID getCurrentUserId() {
        Authentication authentication = getCurrentPrincipal();
        if (authentication == null) {
            return null;
        }
        return UUID.fromString(authentication.getName());
    }
}
