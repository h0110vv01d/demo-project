package org.h0110w.som.core.service;

import org.h0110w.som.core.common.AbstractTestWithDbContainer;
import org.h0110w.som.core.common.test_utils.Cleaner;
import org.h0110w.som.core.configuration.security.AdminInitializer;
import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.tasks.Task;
import org.h0110w.som.core.model.tasks.TaskDto;
import org.h0110w.som.core.model.tasks.TaskStatus;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.model.user.UserDto;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.h0110w.som.core.repository.TasksRepository;
import org.hibernate.LazyInitializationException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TasksServiceTest extends AbstractTestWithDbContainer {
    private final CreateUserAccountRequestDto testUser1 = new CreateUserAccountRequestDto("testUser1", "123qwe", UserAccountType.REGULAR);
    private final CreateUserAccountRequestDto testUser2 = new CreateUserAccountRequestDto("testUser2", "123qwe", UserAccountType.REGULAR);
    private final CreateUserAccountRequestDto testUser3 = new CreateUserAccountRequestDto("testUser3", "123qwe", UserAccountType.REGULAR);
    private UUID testUser1Id = UUID.randomUUID();
    private UUID testUser2Id = UUID.randomUUID();
    private UUID testUser3Id = UUID.randomUUID();
    private Task testTask;
    private Set<UUID> userIds = Set.of(testUser1Id, testUser2Id, testUser3Id);

    private static final String testName = "testName";
    private static final String testDescription = "testDescription";

    @Autowired
    private TasksService tasksService;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private UsersService usersService;

    @MockBean
    private AdminInitializer adminInitializer;

    @MockBean
    private KeycloakUsersService keycloakUsersService;

    @MockBean
    private CustomSecurityContext securityContext;

    @Autowired
    private Cleaner cleaner;

    @BeforeAll
    void beforeAll() {
        cleaner.deleteTasks();
        cleaner.deleteUsersFromDB();
        when(keycloakUsersService.create(testUser1)).thenReturn(testUser1Id);
        when(keycloakUsersService.create(testUser2)).thenReturn(testUser2Id);
        when(keycloakUsersService.create(testUser3)).thenReturn(testUser3Id);
        userAccountsService.createFromDto(testUser1);
        userAccountsService.createFromDto(testUser2);
        userAccountsService.createFromDto(testUser3);
    }

    @BeforeEach
    public void beforeEach() {
        when(securityContext.getCurrentUser()).thenReturn(userAccountsService.getById(testUser1Id));
        cleaner.deleteTasks();
    }

    @Test
    void testCreateTask() {
        TaskDto taskDto = createTestTaskDto();

        taskDto = tasksService.create(taskDto);

        assertNotNull(taskDto);
        assertNotNull(taskDto.getId());
        assertEquals(testName, taskDto.getName());
        assertEquals(testDescription, taskDto.getDescription());
        assertNotNull(taskDto.getCreated());
        assertNotNull(taskDto.getUpdated());
        assertNotNull(taskDto.getCreatedBy());
        assertEquals(testUser1Id, taskDto.getCreatedBy().getId());
        Assertions.assertEquals(TaskStatus.OPEN, taskDto.getStatus());
        assertNotNull(taskDto.getResponsible());
        assertEquals(testUser1Id, taskDto.getResponsible().getId());
        assertNull(taskDto.getSuperTaskId());
        assertNotNull(taskDto.getWatchers());
        assertThat(taskDto.getWatchers().isEmpty()).isTrue();
        assertNotNull(taskDto.getSubTaskIds());
        assertThat(taskDto.getSubTaskIds().isEmpty()).isTrue();

        Task task = tasksService.getById(taskDto.getId());

        assertNotNull(task);
        assertNotNull(task.getId());
        assertEquals(testName, task.getName());
        assertEquals(testDescription, task.getDescription());
        assertNotNull(task.getCreated());
        assertNotNull(task.getUpdated());
        Assertions.assertEquals(testUser1Id, task.getCreatedBy().getId());
        assertEquals(TaskStatus.OPEN, task.getStatus());
        assertNotNull(task.getResponsible());
        Assertions.assertEquals(testUser1Id, task.getResponsible().getId());
        assertNull(task.getSuperTask());
        assertNotNull(task.getWatchers());
        assertThat(task.getWatchers().isEmpty()).isTrue();
        assertThrows(LazyInitializationException.class, () -> task.getSubTasks().isEmpty());

        User user = usersService.getById(testUser1Id);
        assertThrows(LazyInitializationException.class, () -> user.getTasks().isEmpty());
        assertThrows(LazyInitializationException.class, () -> user.getWatchedTasks().isEmpty());
        Set<Task> userTasks = tasksService.getTasksAssignedOnUser(testUser1Id);
        assertNotNull(userTasks);
        assertThat(userTasks.isEmpty()).isFalse();
        assertEquals(1, userTasks.size());
        assertEquals(task, userTasks.stream().findFirst().get());
    }

    @Test
    void testCreateTaskWithEmptyName() {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(null);

        assertThrows(ServiceException.class, () -> tasksService.create(taskDto));
    }

    @Test
    void testUpdateTask() {
        TaskDto taskDto = createTestTaskDto();

        TaskDto beforeUpdateDto = tasksService.create(taskDto);


        TaskDto updateDto = new TaskDto();
        String newName = "newName";
        String newDescription = "newDescription";
        TaskStatus newStatus = TaskStatus.DONE;
        updateDto.setId(beforeUpdateDto.getId());
        updateDto.setName(newName);
        updateDto.setDescription(newDescription);
        updateDto.setResponsible(new UserDto().setId(testUser2Id));
        updateDto.setStatus(newStatus);

        taskDto = tasksService.update(updateDto);

        assertNotNull(taskDto);
        assertNotNull(taskDto.getId());
        assertEquals(beforeUpdateDto.getId(), taskDto.getId());
        assertEquals(newName, taskDto.getName());
        assertEquals(newDescription, taskDto.getDescription());
        assertNotNull(taskDto.getCreated());
        assertEquals(beforeUpdateDto.getCreated(), taskDto.getCreated());
        assertNotNull(taskDto.getUpdated());
//        assertNotEquals(beforeUpdateDto.getUpdated(), taskDto.getUpdated());
        assertNotNull(taskDto.getCreatedBy());
        assertEquals(testUser1Id, taskDto.getCreatedBy().getId());
        assertEquals(newStatus, taskDto.getStatus());
        assertNotNull(taskDto.getResponsible());
        assertEquals(testUser2Id, taskDto.getResponsible().getId());
        assertNull(taskDto.getSuperTaskId());
        assertNotNull(taskDto.getWatchers());
        assertThat(taskDto.getWatchers().isEmpty()).isTrue();
        assertNotNull(taskDto.getSubTaskIds());
        assertThat(taskDto.getSubTaskIds().isEmpty()).isTrue();

        Task task = tasksService.getById(taskDto.getId());

        assertNotNull(task);
        assertNotNull(task.getId());
        assertEquals(beforeUpdateDto.getId(), task.getId());
        assertEquals(newName, task.getName());
        assertEquals(newDescription, task.getDescription());
        assertNotNull(task.getCreated());
        assertEquals(beforeUpdateDto.getCreated(), task.getCreated());
        assertNotNull(task.getUpdated());
        assertNotEquals(beforeUpdateDto.getUpdated(), task.getUpdated());
        assertNotNull(task.getCreatedBy());
        Assertions.assertEquals(testUser1Id, task.getCreatedBy().getId());
        assertEquals(newStatus, task.getStatus());
        assertNotNull(task.getResponsible());
        Assertions.assertEquals(testUser2Id, task.getResponsible().getId());
        assertNull(task.getSuperTask());
        assertNotNull(task.getWatchers());
        assertThat(task.getWatchers().isEmpty()).isTrue();
        assertNotNull(task.getSubTasks());
        assertThrows(LazyInitializationException.class, () -> task.getSubTasks().isEmpty());

        User user = usersService.getById(testUser1Id);
        assertThrows(LazyInitializationException.class, () -> user.getTasks().isEmpty());
        assertThrows(LazyInitializationException.class, () -> user.getWatchedTasks().isEmpty());
        Set<Task> userTasks = tasksService.getTasksAssignedOnUser(testUser1Id);
        assertNotNull(userTasks);
        assertTrue(userTasks.isEmpty());

        Set<Task> user2Tasks = tasksService.getTasksAssignedOnUser(testUser2Id);
        assertNotNull(user2Tasks);
        assertThat(user2Tasks.isEmpty()).isFalse();
        assertEquals(1, user2Tasks.size());
        assertEquals(task, user2Tasks.stream().findFirst().get());
    }

    @Test
    void testCreateSubtasks() {
        TaskDto superTaskDto = tasksService.create(createTestTaskDto());

        TaskDto subTaskDto = createTestTaskDto();
        subTaskDto.setSuperTaskId(superTaskDto.getId());
        for (int i = 0; i < 3; i++) {
            subTaskDto = tasksService.create(subTaskDto);
            assertNotNull(subTaskDto);
            assertNotNull(subTaskDto.getId());
            assertNotNull(subTaskDto.getSuperTaskId());
            assertEquals(superTaskDto.getId(), subTaskDto.getSuperTaskId());
        }

        Task superTask = tasksService.getById(superTaskDto.getId());
        Set<Task> subTasks = tasksService.getSubtasks(superTaskDto.getId());
        assertNotNull(subTasks);
        assertThat(subTasks.isEmpty()).isFalse();
        assertEquals(3, subTasks.size());
        subTasks.forEach(task -> {
            assertEquals(superTask, task.getSuperTask());
        });
    }


    @Test
    void testAddWatchers() {
        TaskDto taskDto = tasksService.create(createTestTaskDto());

        taskDto = tasksService.addWatchers(taskDto.getId(), userIds);

        assertNotNull(taskDto);
        assertNotNull(taskDto.getWatchers());
        assertThat(taskDto.getWatchers().stream()
                .map(UserDto::getId)
                .collect(Collectors.toSet())
                .containsAll(userIds)).isTrue();

        Task task = tasksService.getById(taskDto.getId());
        assertNotNull(task);
        Set<User> watchers = task.getWatchers();
        assertNotNull(watchers);
        assertEquals(3, watchers.size());
        assertThat(watchers.stream()
                .map(User::getId)
                .collect(Collectors.toSet())
                .containsAll(userIds)).isTrue();

        User user = usersService.getById(testUser1Id);
        assertThrows(LazyInitializationException.class, ()->user.getTasks().isEmpty());

        Set<Task> tasks = tasksService.getTasksWatchedByUser(testUser1Id);
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(taskDto.getId(), tasks.stream().findFirst().get().getId());
    }

    @Test
    void testDeleteTasks() {
        TaskDto taskDto = tasksService.create(createTestTaskDto());
        for (int i = 0; i < 3; i++) {
            TaskDto subtaskDto = createTestTaskDto();
            subtaskDto.setSuperTaskId(taskDto.getSuperTaskId());
        }
        tasksService.addWatchers(taskDto.getId(), userIds);

        tasksService.delete(taskDto.getId());

        Set<Task> tasksAssignedOnUser = tasksService.getTasksAssignedOnUser(testUser1Id);
        assertNotNull(tasksAssignedOnUser);
        assertThat(tasksAssignedOnUser.isEmpty());
        Set<Task> watchedTasks = tasksService.getTasksWatchedByUser(testUser1Id);
        assertNotNull(watchedTasks);
        assertThat(watchedTasks.isEmpty());
        List<Task> allTasks = (List<Task>) tasksRepository.findAll();
        assertThat(allTasks.isEmpty()).isTrue();
    }
    //todo
    @Test
    void testDeleteUsers(){

    }
    @NotNull
    private TaskDto createTestTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(testName);
        taskDto.setDescription(testDescription);
        taskDto.setResponsible(new UserDto().setId(testUser1Id));
        return taskDto;
    }
}