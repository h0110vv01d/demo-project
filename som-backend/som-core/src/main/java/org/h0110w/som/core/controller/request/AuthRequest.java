package org.h0110w.som.core.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotNull(message = "{validation.login.not.null}")
    @Size(min = 3, max = 256, message = "{validation.login.size}")
    private String username;

    @NotNull(message = "{validation.password.not.null}")
    private String password;

    @NotNull(message = "{validation.clientId.not.null}")
    private String clientId;

    @NotNull(message = "{validation.clientSecret.not.null}")
    private String clientSecret;
}
