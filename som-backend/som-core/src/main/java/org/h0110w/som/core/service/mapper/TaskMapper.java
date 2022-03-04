package org.h0110w.som.core.service.mapper;

import org.h0110w.som.core.model.tasks.Task;
import org.h0110w.som.core.model.tasks.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = UserMapper.class)
public interface TaskMapper extends AbstractMapper<Task, TaskDto> {
    String tasksToTaskIds = "tasksToTaskIds";
    String taskToTaskId = "taskToTaskId";

    @Override
    @Mapping(source = "superTask", target = "superTaskId", qualifiedByName = taskToTaskId)
    @Mapping(source = "subTasks", target = "subTaskIds", qualifiedByName = tasksToTaskIds)
    TaskDto toDto(Task task);

    @Named(tasksToTaskIds)
    static Set<Long> tasksToTaskIds(Set<Task> tasks) {
        return tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
    }

    @Named(taskToTaskId)
    static Long taskToTaskId(Task task) {
        if (task != null) {
            return task.getId();
        }
        return null;
    }
}
