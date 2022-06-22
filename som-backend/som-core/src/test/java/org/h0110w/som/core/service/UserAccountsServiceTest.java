package org.h0110w.som.core.service;

import org.h0110w.som.core.common.AbstractTestWithDbAndKeycloak;
import org.h0110w.som.core.common.test_utils.Cleaner;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.configuration.security.keycloak.KeycloakConfig;
import org.h0110w.som.core.controller.request.AuthRequest;
import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.controller.request.UpdateUserRequestDto;
import org.h0110w.som.core.repository.UsersRepository;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.model.user_account.UserAccount;
import org.h0110w.som.core.model.user_account.UserAccountDto;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.h0110w.som.core.model.user_account.UserAccountStatus;
import org.h0110w.som.core.repository.UserAccountsRepository;
import org.h0110w.som.core.service.mapper.Mapper;
import org.h0110w.som.core.service.util.pagination.CustomPageRequest;
import org.h0110w.som.core.service.util.pagination.PagedResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.h0110w.som.core.common.TestsConfig.TEST_PASSWORD;
import static org.h0110w.som.core.common.TestsConfig.TEST_SECRET_USERS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserAccountsServiceTest extends AbstractTestWithDbAndKeycloak {
    private static final String testLogin = "login";
    private static final UserAccountType testUserRole = UserAccountType.REGULAR;
    private static final CreateUserAccountRequestDto testCreateRequest = new CreateUserAccountRequestDto(testLogin, TEST_PASSWORD, testUserRole);

    @SpyBean
    private UserAccountsRepository userAccountsRepository;

    @Autowired
    private UserAccountsService userAccountService;

    @Autowired
    private KeycloakUsersService keycloakUsersService;

    @Autowired
    private KeycloakAuthClient authClient;

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Autowired
    private UsersRepository usersRepository;

    @MockBean
    private CustomMessageSource messageSource;

    @Autowired
    private Cleaner cleaner;
//    @MockBean
//    private UserContext userContext;
//
//    @Mock
//    private CustomLogger logger;


    @BeforeEach
    public void setup() {
        cleaner.deleteTasks();
        cleaner.deleteUsersFromKeycloakAndDB();
//        when(userContext.getCurrentUser()).thenReturn(testAdmin);
    }

    @Test
    void testCreateUser() {
        UUID id = userAccountService.createFromDto(testCreateRequest).getId();

        checkUser(userAccountService.getById(id));
    }

    @Test
    void throwsServiceExceptionOnCreateDuplicateUser() {
        userAccountService.createFromDto(testCreateRequest);

        assertThrows(ServiceException.class, () -> userAccountService.createFromDto(testCreateRequest));
    }


    @ParameterizedTest
    @MethodSource("provideInvalidPasswords")
    void throwsServiceExceptionOnCreateWithInvalidPassword(String input) {
        CreateUserAccountRequestDto requestDto = new CreateUserAccountRequestDto(testLogin, input, testUserRole);

        assertThrows(ServiceException.class, () -> userAccountService.createFromDto(requestDto));
    }

    @Test
    void testCreateUserByDto() {
        UserAccountDto userAccountDto = userAccountService.createFromDto(testCreateRequest);

        Optional<UserAccount> userOptional = userAccountsRepository.findByLogin(testLogin);
        assertTrue(userOptional.isPresent());
        UserAccount userAccount = userOptional.get();
        checkUser(userAccount);

        assertNotNull(userAccountDto.getId());
        assertEquals(userAccount.getId(), userAccountDto.getId());
        assertEquals(testLogin, userAccountDto.getLogin());
        assertNull(userAccountDto.getPassword());
        assertEquals(testUserRole, userAccountDto.getUserAccountType());
    }


    @Test
    void testUpdateUserByDto() {
        UserAccountDto user = userAccountService.createFromDto(testCreateRequest);

        String newPassword = "newpassword";
        UserAccountType newRole = UserAccountType.ADMIN;

        UpdateUserRequestDto userUpdateDto = new UpdateUserRequestDto();
        userUpdateDto.setId(user.getId());
        userUpdateDto.setPassword(newPassword);
        userUpdateDto.setUserRole(newRole);

        UserAccountDto userAccountDto = userAccountService.updateByDto(userUpdateDto);
        UserAccount updatedUserAccount = userAccountService.getById(userAccountDto.getId());

        assertNotNull(updatedUserAccount.getId());
        assertNotNull(authClient.getToken(new AuthRequest(testLogin, newPassword, keycloakConfig.getUsersClientId(), TEST_SECRET_USERS)));
        assertEquals(updatedUserAccount.getId(), userAccountDto.getId());
        assertEquals(testLogin, updatedUserAccount.getLogin());
        assertEquals(testLogin, updatedUserAccount.getLogin());
        assertEquals(newRole, updatedUserAccount.getUserAccountType());
        assertEquals(UserAccountStatus.ACTIVE, userAccountDto.getUserAccountStatus());
    }


    @Test
    void testUpdateUserByDtoInvalidPassword() {
        UUID id = userAccountService.createFromDto(testCreateRequest).getId();

        String newPassword = "123";
        UpdateUserRequestDto userUpdateDto = new UpdateUserRequestDto();
        userUpdateDto.setId(id);
        userUpdateDto.setPassword(newPassword);

        assertThrows(ServiceException.class, () -> userAccountService.updateByDto(userUpdateDto));
    }

    @Test
    void testUpdateUserWithNoChanges() {
        KeycloakUsersService keycloakSpy = Mockito.spy(keycloakUsersService);
        Mockito.clearInvocations(userAccountsRepository);

        UUID id = userAccountService.createFromDto(testCreateRequest).getId();

        UpdateUserRequestDto userUpdateDto = new UpdateUserRequestDto();
        userUpdateDto.setId(id);
        userUpdateDto.setPassword(null);
        userUpdateDto.setUserRole(testUserRole);

        userAccountService.updateByDto(userUpdateDto);

        verify(userAccountsRepository, atMostOnce()).save(any());
        verify(keycloakSpy, never()).updatePassword(any(), any());
        verify(keycloakSpy, never()).assignRoleToUser(any(), any());
    }

    @Test
    void testGetUser() {
        UserAccount userAccount = Mapper.USER_ACCOUNT.toEntity(userAccountService.createFromDto(testCreateRequest));

        UserAccount userAccountFoundById = userAccountService.getById(userAccount.getId());
        UserAccount userAccountFoundByLogin = userAccountService.getByLogin(userAccount.getLogin());

        assertEquals(userAccount, userAccountFoundById);
        assertEquals(userAccount, userAccountFoundByLogin);
    }

    @Test
    void throwsServiceExceptionOnGetUserWithNullArgument() {
        assertThrows(ServiceException.class, () -> userAccountService.getById(null));
        assertThrows(ServiceException.class, () -> userAccountService.getByLogin(null));
    }

    @Test
    void throwsServiceExceptionOnGetUserWithInvalidId() {
        UserAccountDto user = userAccountService.createFromDto(testCreateRequest);

        assertThrows(ServiceException.class, () -> userAccountService.getById(UUID.randomUUID()));
        String login = user.getLogin() + "123";
        assertThrows(ServiceException.class, () -> userAccountService.getByLogin(login));
    }

    @Test
    void testDeleteUser() {
        UserAccountDto userAccountDto = userAccountService.createFromDto(testCreateRequest);

        userAccountService.delete(userAccountDto.getId());

        assertFalse(userAccountsRepository.findByLogin(userAccountDto.getLogin()).isPresent());
    }

    @Test
    void testDisableUser() {
        UUID id = userAccountService.createFromDto(testCreateRequest).getId();

        userAccountService.disable(id);

        UserRepresentation userRepresentation = keycloakUsersService.getUserById(id.toString());
        assertFalse(userRepresentation.isEnabled());

        Optional<UserAccount> user = userAccountsRepository.findById(id);
        assertTrue(user.isPresent());
        assertFalse(user.get().isUserActive());
    }

    @Test
    void testEnableUser() {
        UUID id = userAccountService.createFromDto(testCreateRequest).getId();

        userAccountService.disable(id);
        userAccountService.enable(id);

        UserRepresentation userRepresentation = keycloakUsersService.getUserById(id.toString());
        assertTrue(userRepresentation.isEnabled());

        Optional<UserAccount> user = userAccountsRepository.findById(id);
        assertTrue(user.isPresent());
        assertFalse(user.get().isUserActive());
    }

    @Test
    void throwsServiceExceptionOnDisablingDisabledUser() {
        UUID id = userAccountService.createFromDto(testCreateRequest).getId();

        userAccountService.disable(id);

        assertThrows(ServiceException.class, () -> userAccountService.disable(id));
    }

    @Test
    void throwsServiceExceptionOnEnablingEnabledUser() {
        UUID id = userAccountService.createFromDto(testCreateRequest).getId();

        assertThrows(ServiceException.class, () -> userAccountService.enable(id));
    }

    @Test
    void throwsIllegalArgumentExceptionOnDeleteBuiltinUser() {
        Optional<UserAccount> optionalUser = userAccountsRepository.findAll().stream()
                .filter(UserAccount::isBuiltIn)
                .findFirst();

        assertTrue(optionalUser.isPresent());
        UUID id = optionalUser.get().getId();
        assertThrows(IllegalArgumentException.class, () -> userAccountService.delete(id));
    }


    @Test
    void testPagedRequest() {
        UserAccountDto user = userAccountService.createFromDto(testCreateRequest);
        List<UserAccount> userAccounts = userAccountsRepository.findAll();

        assertEquals(2, userAccounts.size());
        assertEquals(1, userAccounts.stream().filter(UserAccount::isBuiltIn).count());

        PagedResult<UserAccountDto> pagedResult = userAccountService.paged(new CustomPageRequest());

        assertNotNull(pagedResult);
        List<UserAccountDto> userAccountDtos = pagedResult.getItems();
        assertEquals(1, userAccountDtos.size());
        assertEquals(user.getLogin(), userAccountDtos.get(0).getLogin());
    }

    private void checkUser(UserAccount userAccount) {
        assertNotNull(userAccount);
        assertNotNull(userAccount.getId());
        assertEquals(testLogin, userAccount.getLogin());
        assertEquals(testUserRole, userAccount.getUserAccountType());
        assertEquals(UserAccountStatus.ACTIVE, userAccount.getUserAccountStatus());
        assertTrue(userAccount.isUserActive());
        assertFalse(userAccount.isBuiltIn());
        assertNotNull(keycloakUsersService.getUserById(userAccount.getId().toString()));

        User user = usersRepository.findById(userAccount.getId()).get();
        assertNotNull(user);
        assertEquals(UsersService.UNNAMED, user.getDisplayName());
    }

    private static Stream<Arguments> provideInvalidPasswords() {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of("  "),
                Arguments.of("12345")
        );
    }
}




