package org.h0110w.som.core.service;

import org.h0110w.som.core.configuration.security.keycloak.KeycloakConfig;
import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.exception.InternalServiceException;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.service.util.UserToKeycloakUserRepresentationConverter;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KeycloakUsersService {
    private final KeycloakConfig keycloakConfig;
    private final Keycloak keycloakAdminClient;

    @Autowired
    public KeycloakUsersService(Keycloak keycloakAdminClient, KeycloakConfig keycloakConfig) {
        this.keycloakAdminClient = keycloakAdminClient;
        this.keycloakConfig = keycloakConfig;
    }

    public UUID create(CreateUserAccountRequestDto userDto) {
        Response response = null;
        response = keycloakUsers().create(UserToKeycloakUserRepresentationConverter.convertCreateRequestToUserRep(userDto));

        if (response.getStatus() > 300) {
            String message = "error while creating user in keycloak with login: " + userDto.getLogin();
            throw new ServiceException(message);
        }
        List<UserRepresentation> users = keycloakAdminClient
                .realm(keycloakConfig.getRealm())
                .users()
                .search(userDto.getLogin())
                .stream()
                .filter(userRepresentation ->
                        userRepresentation.getUsername()
                                .equalsIgnoreCase(userDto.getLogin()))
                .collect(Collectors.toList());
        String id = getUserIdOrThrowException(users, userDto);
        assignRoleToUser(userDto.getUserRole(), id);
        return UUID.fromString(id);
    }

    public void assignRoleToUser(UserAccountType userRole, String id) {
        RoleRepresentation role = keycloakRealm()
                .roles()
                .get(userRole.toString())
                .toRepresentation();
        keycloakUsers().get(id)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

    public void updatePassword(String userId, String newPassword) {
        UserResource user = keycloakUsers().get(userId);
        UserRepresentation updatedUser = user.toRepresentation();

        updatedUser.setCredentials(UserToKeycloakUserRepresentationConverter.createCredentials(newPassword));

        user.update(updatedUser);
    }

    public void disable(UUID userId) {
        UserResource user = keycloakUsers().get(userId.toString());
        UserRepresentation updatedUser = user.toRepresentation();

        updatedUser.setEnabled(false);

        user.update(updatedUser);
    }

    public void enable(String userId) {
        UserResource user = keycloakUsers().get(userId);
        UserRepresentation updatedUser = user.toRepresentation();

        updatedUser.setEnabled(true);

        user.update(updatedUser);
    }

    public void delete(String id) {
        keycloakUsers().delete(id);
    }

    public List<UserRepresentation> search(String username) {
        return keycloakUsers().search(username);
    }

    public List<UserRepresentation> findAll() {
        return keycloakUsers().list();
    }

    public UserRepresentation getUserById(String id) {
        return keycloakUsers().get(id).toRepresentation();
    }

    public UUID getAdminId() {
        List<UserRepresentation> users = search(keycloakConfig.getSomRealmAdminUsername());
        if (users.isEmpty()) {
            throw new BeanInitializationException("keycloak admin doesn't exist");
        } else if ((long) users.size() > 1) {
            throw new BeanInitializationException("there are more than 1 users with admin username");
        }
        return UUID.fromString(users.get(0).getId());
    }

    public void createKeycloakAdmin() {
        CreateUserAccountRequestDto admin = new CreateUserAccountRequestDto();
        admin.setLogin(keycloakConfig.getSomRealmAdminUsername());
        admin.setPassword(keycloakConfig.getAdminPassword());
        admin.setUserRole(UserAccountType.ADMIN);
        create(admin);
    }

    private String getUserIdOrThrowException(List<UserRepresentation> users, CreateUserAccountRequestDto userDto) {
        if (!users.isEmpty()) {
            if (users.size() > 1) {
                String message = "There is more than 1 user in keycloak realm with the same login: "
                        + userDto.getLogin();
                throw new InternalServiceException(message);
            }
            String id = users.get(0).getId();
            if (StringUtils.isEmpty(id)){
                throw new InternalServiceException("Keycloak returned invalid id");
            }
            return id;
        } else {
            String message = "Error creating user in keycloak with username: "
                    + userDto.getLogin();
            throw new InternalServiceException(message);
        }
    }

    private UsersResource keycloakUsers() {
        return keycloakRealm()
                .users();
    }

    private RealmResource keycloakRealm() {
        return keycloakAdminClient
                .realm(keycloakConfig.getRealm());
    }
}
