package org.h0110w.som.core.model.user_account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h0110w.som.core.model.user.User;

import javax.persistence.*;
import java.util.UUID;

/**
 * system entity containing data not related to business logic
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_accounts")
public class UserAccount {
    @Id
    private UUID id;

    private String login;

    /**
     * defines which api user can access and which are not
     */
    @Enumerated(EnumType.STRING)
    private UserAccountType userAccountType;

    /**
     * defines whether user able to use an app or not
     */
    @Enumerated(EnumType.STRING)
    @Column
    private UserAccountStatus userAccountStatus;

    @Column
    private boolean builtIn;

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User user;

    @JsonIgnore
    public boolean isUserActive() {
        return userAccountStatus.equals(UserAccountStatus.ACTIVE);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserAccount)) {
            return false;
        }
        UserAccount otherUserAccount = (UserAccount) o;
        return this.id.equals(otherUserAccount.id);
    }
}
