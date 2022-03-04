package org.h0110w.som.core.service.mapper;

import org.h0110w.som.core.model.user.UserDto;
import org.h0110w.som.core.model.Group;
import org.h0110w.som.core.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface UserMapper extends AbstractMapper<User, UserDto> {
    String groupToGroupId = "groupToGroupId";

    @Override
    @Mapping(source = "group", target = "groupId", qualifiedByName = groupToGroupId)
    @Mapping(source = "controlledGroup", target = "controlledGroupId", qualifiedByName = groupToGroupId)
    UserDto toDto(User user);

    @Named(groupToGroupId)
    static UUID groupToGroupId(Group group) {
        if (group != null) {
            return group.getId();
        }
        return null;
    }
}
