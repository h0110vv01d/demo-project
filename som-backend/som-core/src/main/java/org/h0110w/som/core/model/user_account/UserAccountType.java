package org.h0110w.som.core.model.user_account;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * enum class used to separate users in groups by set of permissions
 * that define what api users can use and which are not
 */
public enum UserAccountType {
    ADMIN(Set.of(
            UserPermission.USER_CRUD)),
    REGULAR(Set.of(UserPermission.USER_CRUD));
    private final Set<UserPermission> permissions;

    UserAccountType(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.name()))
                .collect(Collectors.toList());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
