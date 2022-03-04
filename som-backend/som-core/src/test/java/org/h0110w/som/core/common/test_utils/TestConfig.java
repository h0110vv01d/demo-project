package org.h0110w.som.core.common.test_utils;

import org.h0110w.som.core.repository.TasksRepository;
import org.h0110w.som.core.repository.GroupsRepository;
import org.h0110w.som.core.repository.UserAccountsRepository;
import org.h0110w.som.core.service.GroupsService;
import org.h0110w.som.core.service.KeycloakUsersService;
import org.h0110w.som.core.service.TasksService;
import org.h0110w.som.core.service.UserAccountsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public Cleaner cleaner(KeycloakUsersService keycloakUsersService,
                           UserAccountsRepository userAccountsRepository, UserAccountsService userAccountService,
                           GroupsRepository groupsRepository, GroupsService groupsService,
                           TasksRepository tasksRepository, TasksService tasksService
    ) {
        return new Cleaner(keycloakUsersService,
                userAccountsRepository, userAccountService,
                groupsRepository, groupsService,
                tasksRepository, tasksService);
    }
}
