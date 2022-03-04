package org.h0110w.som.core.model.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String displayName;
    private UUID groupId;
    private UUID controlledGroupId;
    private String roleName;
    private PersonalData personalData;

    public UserDto setId(UUID id) {
        this.id = id;
        return this;
    }
}
