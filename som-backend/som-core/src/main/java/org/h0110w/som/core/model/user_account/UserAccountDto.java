package org.h0110w.som.core.model.user_account;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * dto that contains data about some user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
    private UUID id;

    @NotNull(message = "{validation.login.not.null}")
    @Size(min = 3, max = 256, message = "{validation.login.size}")
    private String login;

    private String password;

    @NotNull
    private UserAccountType userAccountType;

    private UserAccountStatus userAccountStatus;

}
