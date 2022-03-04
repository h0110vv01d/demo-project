package org.h0110w.som.core.service.util;

import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;

public class UserToKeycloakUserRepresentationConverter {
    private UserToKeycloakUserRepresentationConverter(){

    }
    public static UserRepresentation convertCreateRequestToUserRep(CreateUserAccountRequestDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getLogin());
        user.setCredentials(createCredentials(userDto.getPassword()));
        // for some reason setting realm roles has no effect
        user.setRealmRoles(Collections.singletonList(userDto.getUserRole().toString()));
        user.setEnabled(true);
        return user;
    }

    public static List<CredentialRepresentation> createCredentials(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return Collections.singletonList(passwordCred);
    }
}
