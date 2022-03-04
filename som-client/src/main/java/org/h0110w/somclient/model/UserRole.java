package org.h0110w.somclient.model;


import java.util.Set;


public enum UserRole {
    ADMIN(Set.of(
            UserPermission.USER_CRUD)),
    USER(Set.of(UserPermission.USER_CRUD));
    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

}
