package org.h0110w.som.core.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.h0110w.som.core.model.user_account.UserAccountType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * dto used to update user
 * id of user to update must not be null or empty
 * if password and UserRole are empty, user would not be updated
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {
    @NotNull(message = "{validation.id.not.null}")
    private UUID id;
    private String password;
    private UserAccountType userRole;
}
