package org.h0110w.som.core.configuration.security.keycloak;

import org.h0110w.som.core.common.AbstractTestWithDbAndKeycloak;
import org.h0110w.som.core.common.test_utils.Cleaner;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.controller.UserAccountsController;
import org.h0110w.som.core.controller.request.AuthRequest;
import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.model.user_account.UserAccountDto;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.h0110w.som.core.service.KeycloakUsersService;
import org.h0110w.som.core.service.UserAccountsService;
import org.h0110w.som.core.service.mapper.Mapper;
import org.h0110w.som.core.service.util.pagination.CustomPageRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.h0110w.som.core.common.TestsConfig.TEST_PASSWORD;
import static org.h0110w.som.core.common.TestsConfig.TEST_SECRET_USERS;
import static org.h0110w.som.core.common.test_utils.RequestBodyConstructor.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeycloakAuthTest extends AbstractTestWithDbAndKeycloak {
    public static final CreateUserAccountRequestDto testLocalUser = new CreateUserAccountRequestDto("testUser", TEST_PASSWORD, UserAccountType.REGULAR);
    public static final CreateUserAccountRequestDto testBlockedUser = new CreateUserAccountRequestDto("testBlockedUser", TEST_PASSWORD, UserAccountType.ADMIN);
    private static final String AUTH_URL = "/auth/token";
    private static final String AUTH_MY_ACCOUNT = "/auth/my-account";
    private static final String USERS_PAGED_URL = UserAccountsController.URL + "/paged";

    private UserAccountDto testLocalUserDto;

    @Autowired
    private KeycloakUsersService keycloakUsersService;

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Cleaner cleaner;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private MockMvc mockMvc;

    @Autowired
    private UserAccountsController userController;

    @Autowired
    private UserAccountsService userAccountsService;

//    @MockBean
//    private UserAccountsService userService;
//
//    @MockBean
//    private UserAccountsRepository userAccountsRepository;

    @MockBean
    private CustomMessageSource messageSource;

//    @MockBean
//    private CustomLogger logger;

    @BeforeAll
    public void setup() {
        cleaner.deleteUsersFromKeycloakAndDB();
        testLocalUserDto = userAccountsService.createFromDto(testLocalUser);
        userAccountsService.disable(userAccountsService.createFromDto(testBlockedUser).getId());

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    void testLoginThrowsClientErrorOnInvalidUser() throws Exception {
        String requestBody = asJsonString(authRequest("user1"));
        mockMvc.perform(
                        MockMvcRequestBuilders.post(AUTH_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void testObtainToken() throws Exception {
        String requestBody = asJsonString(authRequest(keycloakConfig.getSomRealmAdminUsername()));
        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

    }

    @Test
    void throws400ClientErrorOnBlockedUserAuth() throws Exception {
        String requestBody = asJsonString(authRequest(testBlockedUser.getLogin()));
        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // keycloak returns 400, without specifying the reason
                .andExpect(status().isBadRequest());
    }

    @Test
    void throws400ClientErrorOnInvalidRequestBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new AuthRequest())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwsClientErrorOnRequestWithoutAuthHeader() throws Exception {
        String requestBody = asJsonString(new CustomPageRequest());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(USERS_PAGED_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123"})
    void throwsClientErrorOnRequestWithInvalidAuthHeader(String headerValue) throws Exception {
        String requestBody = asJsonString(new CustomPageRequest());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(USERS_PAGED_URL)
                                .header(keycloakConfig.getAuthorizationHeader(), headerValue)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void throwsRuntimeExceptionOnRequestWithInvalidToken() throws Exception {
        String requestBody = asJsonString(new CustomPageRequest());
        String authHeader = keycloakConfig.getTokenPrefix() + "qweqweqwe";

        mockMvc.perform(MockMvcRequestBuilders.post(USERS_PAGED_URL)
                        .header(keycloakConfig.getAuthorizationHeader(), authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void successfulRequestWithCorrectAuthHeader() throws Exception {
        String requestBody = asJsonString(new CustomPageRequest());
        String token = obtainAdminToken();

        mockMvc.perform(MockMvcRequestBuilders.post(USERS_PAGED_URL)
                        .header(keycloakConfig.getAuthorizationHeader(), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void throwsClientErrorOnRequestToForbiddenApi() throws Exception {
        String requestBody = asJsonString(new CustomPageRequest());
        String token = obtainToken(testLocalUser.getLogin());

        mockMvc.perform(MockMvcRequestBuilders.post(USERS_PAGED_URL)
                        .header(keycloakConfig.getAuthorizationHeader(), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCustomSecurityContext() throws Exception {
        String token = obtainToken(testLocalUser.getLogin());

        String json = mockMvc.perform(MockMvcRequestBuilders.get(AUTH_MY_ACCOUNT)
                        .header(keycloakConfig.getAuthorizationHeader(), token))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(json);
        UserAccountDto result = Mapper.getMapper().readValue(json, UserAccountDto.class);
        assertNotNull(result);
        assertEquals(testLocalUserDto.getId(), result.getId());
        assertEquals(testLocalUserDto.getLogin(), result.getLogin());
        assertEquals(testLocalUserDto.getUserAccountType(), result.getUserAccountType());
        assertEquals(testLocalUserDto.getUserAccountStatus(), result.getUserAccountStatus());
        assertNull(result.getPassword());
    }

    private AuthRequest authRequest(String login) {
        return new AuthRequest(login, TEST_PASSWORD,
                keycloakConfig.getUsersClientId(), TEST_SECRET_USERS);
    }

    private String obtainAdminToken() throws Exception {
        return obtainToken(keycloakConfig.getSomRealmAdminUsername());
    }

    private String obtainToken(String login) throws Exception {
        String requestBody = asJsonString(authRequest(login));
        String responseContentAsString = mockMvc.perform(
                        MockMvcRequestBuilders.post(AUTH_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        String token = Mapper.getMapper().readTree(responseContentAsString)
                .get("accessToken").asText();
        return keycloakConfig.getTokenPrefix() + token;
    }

}