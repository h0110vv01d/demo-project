package org.h0110w.som.core.controller.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.h0110w.som.core.model.user_account.UserAccountType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Dto used to create user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserAccountRequestDto {
    @NotNull(message = "{validation.login.not.null}")
    @Size(min = 3, max = 256, message = "{validation.login.size}")
    private String login;

    @NotNull(message = "{validation.password.not.null}")
    @Size(min = 6, max = 256, message = "{validation.password.size}")
    private String password;

    @NotNull(message = "{validation.role.not.null}")
    private UserAccountType userRole;
}
