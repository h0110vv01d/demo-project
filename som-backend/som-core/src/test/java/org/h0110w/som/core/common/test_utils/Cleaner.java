package org.h0110w.som.core.common.test_utils;

import org.h0110w.som.core.repository.TasksRepository;
import org.h0110w.som.core.repository.GroupsRepository;
import org.h0110w.som.core.repository.UserAccountsRepository;
import org.h0110w.som.core.service.GroupsService;
import org.h0110w.som.core.service.KeycloakUsersService;
import org.h0110w.som.core.service.TasksService;
import org.h0110w.som.core.service.UserAccountsService;

import java.util.Objects;

public class Cleaner {
    private final KeycloakUsersService keycloakUsersService;
    private final UserAccountsRepository userAccountsRepository;
    private final UserAccountsService userAccountService;
    private final GroupsRepository groupsRepository;
    private final GroupsService groupsService;
    private final TasksRepository tasksRepository;
    private final TasksService tasksService;

    public Cleaner(KeycloakUsersService keycloakUsersService,
                   UserAccountsRepository userAccountsRepository, UserAccountsService userAccountService,
                   GroupsRepository groupsRepository, GroupsService groupsService,
                   TasksRepository tasksRepository, TasksService tasksService) {
        this.keycloakUsersService = keycloakUsersService;
        this.userAccountsRepository = userAccountsRepository;
        this.userAccountService = userAccountService;
        this.groupsRepository = groupsRepository;
        this.groupsService = groupsService;
        this.tasksRepository = tasksRepository;
        this.tasksService = tasksService;
    }


    public void deleteUsersFromKeycloakAndDB() {
        deleteUsersFromDB();
        deleteUsersFromKeycloakRealm();
    }

    public void deleteUsersFromKeycloakRealm() {
        String adminId = keycloakUsersService.getAdminId().toString();
        keycloakUsersService.findAll().stream()
                .filter(user -> !Objects.equals(user.getId(), adminId))
                .forEach(u -> keycloakUsersService.delete(u.getId()));
    }

    public void deleteUsersFromDB() {
        userAccountsRepository.findAll().stream()
                .filter(u -> !u.isBuiltIn())
                .forEach(u -> userAccountService.delete(u.getId()));
    }

    public void deleteGroups() {
        groupsRepository.findAll()
                .forEach(group -> groupsService.delete(group.getId()));
    }

    public void deleteTasks() {
        tasksRepository.findAll()
                .forEach(task -> tasksService.delete(task.getId()));
    }
}
