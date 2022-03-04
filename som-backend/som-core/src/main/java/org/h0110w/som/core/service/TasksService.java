package org.h0110w.som.core.service;

import org.apache.commons.lang3.StringUtils;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.tasks.TaskDto;
import org.h0110w.som.core.model.user.UserDto;
import org.h0110w.som.core.repository.TasksRepository;
import org.h0110w.som.core.service.mapper.Mapper;
import org.h0110w.som.core.model.tasks.Task;
import org.h0110w.som.core.model.tasks.TaskStatus;
import org.h0110w.som.core.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

//todo cyclic relationship create/update
@Service
public class TasksService {
    private final TasksRepository tasksRepository;
    private final CustomMessageSource messageSource;
    private final UsersService usersService;
    private final CustomSecurityContext securityContext;

    @Autowired
    public TasksService(TasksRepository tasksRepository, CustomMessageSource messageSource, UsersService usersService, CustomSecurityContext securityContext) {
        this.tasksRepository = tasksRepository;
        this.messageSource = messageSource;
        this.usersService = usersService;
        this.securityContext = securityContext;
    }

    @Transactional
    public TaskDto create(TaskDto taskDto) {
        checkName(taskDto.getName());

        Task task = new Task();

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.OPEN);
        task.setCreatedBy(securityContext.getCurrentUser().getUser());

        setResponsible(task, taskDto);
        setSuperTask(task, taskDto);

        task = tasksRepository.save(task);

        return Mapper.TASK.toDto(task);
    }

    private void setSuperTask(Task task, TaskDto taskDto) {
        Long superTaskId = taskDto.getSuperTaskId();
        if (superTaskId != null) {
            Task superTask = getById(superTaskId);
            task.attachToSuperTask(superTask);
        }
    }

    private void setResponsible(Task task, TaskDto taskDto) {
        UserDto userDto = taskDto.getResponsible();
        if (userDto != null && userDto.getId() != null) {
            User user = usersService.getById(userDto.getId());
            task.assignResponsible(user);
        }
    }

    @Transactional
    public TaskDto update(TaskDto updateDto) {
        Task task = getById(updateDto.getId());

        updateName(updateDto, task);
        updateDescription(updateDto, task);
        updateResponsible(updateDto, task);
        updateStatus(updateDto, task);

        task = tasksRepository.save(task);

        return Mapper.TASK.toDto(task);
    }

    private void updateName(TaskDto updateDto, Task task) {
        if (updateDto.getName() != null) {
            final boolean shouldUpdateName = !isNameInvalid(updateDto.getName()) &&
                    !task.getName().equals(updateDto.getName());
            if (shouldUpdateName) {
                task.setName(updateDto.getName());
            }
        }
    }

    private void updateDescription(TaskDto updateDto, Task task) {
        if (updateDto.getDescription() != null) {
            final boolean shouldUpdateDescription = !isNameInvalid(updateDto.getDescription()) &&
                    !task.getDescription().equals(updateDto.getDescription());
            if (shouldUpdateDescription) {
                task.setDescription(updateDto.getDescription());
            }
        }
    }

    private void updateResponsible(TaskDto updateDto, Task task) {
        if (updateDto.getResponsible() != null && updateDto.getResponsible().getId() != null) {
            final boolean shouldUpdateResponsible = !task.getResponsible().getId()
                    .equals(updateDto.getResponsible().getId());
            if (shouldUpdateResponsible) {
                User user = usersService.getById(updateDto.getResponsible().getId());
                task.assignResponsible(user);
            }
        }
    }

    private void updateStatus(TaskDto updateDto, Task task) {
        if (updateDto.getStatus() != null) {
            final boolean shouldUpdateStatus = !task.getStatus()
                    .equals(updateDto.getStatus());
            if (shouldUpdateStatus) {
                task.setStatus(updateDto.getStatus());
            }
        }
    }

    @Transactional
    public TaskDto addWatchers(Long taskId, Set<UUID> userIds) {
        Task task = getById(taskId);
        userIds.forEach(id -> {
            User user = usersService.getById(id);
            task.addWatcher(user);
        });

        Task updatedTask = tasksRepository.save(task);
        return Mapper.TASK.toDto(updatedTask);
    }

    public Task getById(Long id) {
        if (id == null) {
            throw new ServiceException(messageSource.get("id.is.null"));
        }
        return tasksRepository.findById(id)
                .orElseThrow(() -> new ServiceException(messageSource.get("task.not.found")));

    }

    public Set<Task> getTasksAssignedOnUser(UUID id) {
        return tasksRepository.findByResponsibleId(id);
    }

    public Set<Task> getSubtasks(Long superTaskId) {
        Task task = getById(superTaskId);

        return tasksRepository.findBySuperTaskId(task.getId());
    }

    public Set<Task> getTasksWatchedByUser(UUID userId) {
        User user = usersService.getById(userId);

        return tasksRepository.findByWatcherId(user.getId());
    }

    @Transactional
    public void delete(Long id) {
        Task task = getById(id);

        task.getWatchers().forEach(watcher->watcher.getWatchedTasks().remove(task));

        tasksRepository.delete(task);
    }

    private void checkName(String name) {
        if (isNameInvalid(name)) {
            throw new ServiceException(messageSource.get("task.name.is.null"));
        }
    }

    private boolean isNameInvalid(String name) {
        return StringUtils.isBlank(name);
    }
}
