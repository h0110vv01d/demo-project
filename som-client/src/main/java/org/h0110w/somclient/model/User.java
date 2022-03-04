package org.h0110w.somclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.control.TableRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * User entity represents person who is using an app
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String login;
    private UserRole userRole;
    private UserStatus userStatus;
    private boolean builtIn;

    @JsonIgnore
    public boolean isUserActive() {
        return userStatus.equals(UserStatus.ACTIVE);
    }

    public User(String login, UserRole userRole) {
        this.login = login;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        User otherUser = (User) o;
        return this.id.equals(otherUser.id);
    }
}
