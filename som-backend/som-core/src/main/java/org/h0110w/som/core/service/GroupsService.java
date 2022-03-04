package org.h0110w.som.core.service;

import org.apache.commons.lang3.StringUtils;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.Group;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.repository.UsersRepository;
import org.h0110w.som.core.repository.GroupsRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupsService {
    public static final String UNNAMED = "unnamed";
    private final GroupsRepository groupsRepository;
    private final CustomMessageSource messageSource;
    private final UsersService usersService;

    @Autowired
    public GroupsService(GroupsRepository groupsRepository, CustomMessageSource messageSource, UsersService usersService) {
        this.groupsRepository = groupsRepository;
        this.messageSource = messageSource;
        this.usersService = usersService;
    }

    @Transactional
    public Group create() {
        Group group = new Group();
        group.setName(UNNAMED);

        groupsRepository.save(group);

        return group;
    }
    //todo check cyclic relationship
    @Transactional
    public Group assignLead(UUID groupId, UUID userId) {
        Group group = getById(groupId);
        User user = usersService.getById(userId);

        if (group.getLead() != null) {
            group.getLead().setControlledGroup(null);
        }
        group.assignLead(user);

        return groupsRepository.save(group);
    }

    @Transactional
    public Set<User> getUsersByGroupId(UUID groupId) {
        Group group = getById(groupId);
        Hibernate.initialize(group.getUsers());
        return group.getUsers().stream()
                .map(user -> Hibernate.unproxy(user, User.class))
                .collect(Collectors.toSet());
    }

    public Group updateName(UUID groupId, String name) {
        checkGroupName(name);
        Group group = getById(groupId);

        group.setName(name);

        return groupsRepository.save(group);
    }

    @Transactional
    public void addUsers(UUID groupId, Set<UUID> userIds) {
        Group group = getById(groupId);

        userIds.forEach(uuid -> group.addUser(usersService.getById(uuid)));

        groupsRepository.save(group);
    }

    @Transactional
    public void removeLead(UUID groupId) {
        Group group = getById(groupId);

        if (group.getLead() != null) {
            group.getLead().setControlledGroup(null);
            group.setLead(null);

            groupsRepository.save(group);
        }
    }

    @Transactional
    public void delete(UUID id) {
        Group group = getById(id);

        if (group.getLead() != null) {
            group.getLead().setControlledGroup(null);
            group.setLead(null);
        }
        group.getUsers().forEach(user -> user.setGroup(null));
        group.getUsers().clear();

        groupsRepository.delete(group);
    }

    @Transactional
    public Group getById(UUID id) {
        Optional<Group> group = groupsRepository.findById(id);
        if (!group.isPresent()) {
            throw new ServiceException(messageSource.get("group.not.found"));
        }
        return group.get();
    }

    private void checkGroupName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new ServiceException(messageSource.get("group.name.is.null"));
        }
    }
}
