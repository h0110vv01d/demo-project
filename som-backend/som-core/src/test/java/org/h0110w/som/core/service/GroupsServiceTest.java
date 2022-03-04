package org.h0110w.som.core.service;

import org.h0110w.som.core.common.AbstractTestWithDbContainer;
import org.h0110w.som.core.common.test_utils.Cleaner;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.configuration.security.AdminInitializer;
import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.Group;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupsServiceTest extends AbstractTestWithDbContainer {
    private final CreateUserAccountRequestDto testUser1 = new CreateUserAccountRequestDto("testUser1", "123qwe", UserAccountType.REGULAR);
    private final CreateUserAccountRequestDto testUser2 = new CreateUserAccountRequestDto("testUser2", "123qwe", UserAccountType.REGULAR);
    private final CreateUserAccountRequestDto testUser3 = new CreateUserAccountRequestDto("testUser3", "123qwe", UserAccountType.REGULAR);
    private UUID testUser1Id = UUID.randomUUID();
    private UUID testUser2Id = UUID.randomUUID();
    private UUID testUser3Id = UUID.randomUUID();
    private Set<UUID> userIds = Set.of(testUser1Id, testUser2Id, testUser3Id);;
    private Group testGroup;

    @MockBean
    private AdminInitializer adminInitializer;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserAccountsService userAccountsService;
    @MockBean
    private KeycloakUsersService keycloakUsersService;
    @MockBean
    private CustomMessageSource messageSource;
    @Autowired
    private Cleaner cleaner;


    @BeforeEach
    public void beforeEach() {
        cleaner.deleteGroups();
        cleaner.deleteUsersFromDB();
        when(keycloakUsersService.create(testUser1)).thenReturn(testUser1Id);
        when(keycloakUsersService.create(testUser2)).thenReturn(testUser2Id);
        when(keycloakUsersService.create(testUser3)).thenReturn(testUser3Id);
        userAccountsService.createFromDto(testUser1);
        userAccountsService.createFromDto(testUser2);
        userAccountsService.createFromDto(testUser3);
    }

    @Test
    void testCreateGroup() {
        Group group = groupsService.create();

        assertNotNull(group);
        assertEquals(GroupsService.UNNAMED, group.getName());
        assertNull(group.getLead());
        assertNotNull(group.getUsers());
        assertTrue(group.getUsers().isEmpty());
    }

    @Test
    void testUpdateName() {
        String newName = "newName";
        Group group = groupsService.create();

        assertNotNull(group);
        assertEquals(GroupsService.UNNAMED, group.getName());

        Group updatedGroup = groupsService.updateName(group.getId(), newName);

        assertNotNull(updatedGroup);
        assertEquals(newName, updatedGroup.getName());
    }

    @Test
    void testAssignLead() {
        testGroup = groupsService.create();

        testGroup = groupsService.assignLead(testGroup.getId(), testUser1Id);
        User updatedUser = usersService.getById(testUser1Id);

        assertNotNull(testGroup.getLead());
        Assertions.assertEquals(updatedUser, testGroup.getLead());
        Set<User> users = groupsService.getUsersByGroupId(testGroup.getId());
        assertTrue(users.isEmpty());

        assertNull(updatedUser.getGroup());
        assertNotNull(updatedUser.getControlledGroup());
        assertEquals(updatedUser.getControlledGroup(), testGroup);
    }

    @Test
    void testRemoveLead() {
        testAssignLead();
        groupsService.addUsers(testGroup.getId(), Set.of(testUser1Id));
        groupsService.removeLead(testGroup.getId());

        testGroup = groupsService.getById(testGroup.getId());
        assertNotNull(testGroup);
        assertNull(testGroup.getLead());
        User user = usersService.getById(testUser1Id);
        assertNotNull(user);
        assertNotNull(user.getGroup());
        assertNull(user.getControlledGroup());
        assertEquals(user.getGroup(), testGroup);
        assertTrue(groupsService.getUsersByGroupId(testGroup.getId()).contains(user));
    }

    @Test
    void testAssignNewLead() {
        testAssignLead();

        groupsService.addUsers(testGroup.getId(), Set.of(testUser1Id));
        groupsService.assignLead(testGroup.getId(), testUser2Id);
        testGroup = groupsService.getById(testGroup.getId());

        assertNotNull(testGroup);
        assertNotNull(testGroup.getLead());
        Assertions.assertEquals(testGroup.getLead().getId(), testUser2Id);

        Set<User> users = groupsService.getUsersByGroupId(testGroup.getId());
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertTrue(users.contains(usersService.getById(testUser1Id)));

        User user2 = usersService.getById(testUser2Id);
        assertNotNull(user2);
        assertNull(user2.getGroup());
        assertNotNull(user2.getControlledGroup());
        assertEquals(user2.getControlledGroup(), testGroup);
    }

    @Test
    void testAddUsers() {
        testGroup = groupsService.create();

        groupsService.addUsers(testGroup.getId(), userIds);
        groupsService.assignLead(testGroup.getId(), testUser1Id);

        testGroup = groupsService.getById(testGroup.getId());
        assertNotNull(testGroup);
        Assertions.assertEquals(testGroup.getLead().getId(), testUser1Id);
        Set<User> users = groupsService.getUsersByGroupId(testGroup.getId());
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(3, users.size());
        assertTrue(users.stream()
                .map(User::getId)
                .collect(Collectors.toSet())
                .containsAll(userIds));

    }

    @Test
    void testDeleteGroup() {
        testAddUsers();

        groupsService.delete(testGroup.getId());

        assertThrows(ServiceException.class, () -> groupsService.getById(testGroup.getId()));
        userIds.forEach(uuid -> {
            User user = usersService.getById(uuid);
            assertNull(user.getGroup());
            assertNull(user.getControlledGroup());
        });
    }

    @Test
    void testDeleteUsers(){
        testAddUsers();

        userIds.forEach(id->userAccountsService.delete(id));

        testGroup = groupsService.getById(testGroup.getId());
        assertNotNull(testGroup);
        Set<User> users = groupsService.getUsersByGroupId(testGroup.getId());
        assertNotNull(users);
        assertTrue(users.isEmpty());
        assertNull(testGroup.getLead());
    }
}