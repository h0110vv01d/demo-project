package org.h0110w.som.core.model.tasks;

import lombok.Data;
import org.h0110w.som.core.model.user.UserDto;

import java.util.Date;
import java.util.Set;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private UserDto createdBy;
    private Date created;
    private Date updated;
    private UserDto responsible;
    private Set<UserDto> watchers;
    private TaskStatus status;
    private Long superTaskId;
    private Set<Long> subTaskIds;
}
