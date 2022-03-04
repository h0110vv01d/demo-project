package org.h0110w.som.core.service;

import org.h0110w.som.core.common.AbstractTestWithDbContainer;
import org.h0110w.som.core.common.test_utils.Cleaner;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.configuration.security.AdminInitializer;
import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.repository.UsersRepository;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.user.Contact;
import org.h0110w.som.core.model.user.PersonalData;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.h0110w.som.core.repository.UserAccountsRepository;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UsersServiceTest extends AbstractTestWithDbContainer {
    public static final UUID testUserId = UUID.randomUUID();
    private static final CreateUserAccountRequestDto testUserRequest = new CreateUserAccountRequestDto("testUser", "123qwe", UserAccountType.REGULAR);


    @Autowired
    private UsersService usersService;

    @Autowired
    private UserAccountsService userAccountsService;

    @MockBean
    private KeycloakUsersService keycloakUsersService;

    @MockBean
    private AdminInitializer adminInitializer;

    @Autowired
    private Cleaner cleaner;

    @MockBean
    private CustomMessageSource messageSource;

    @Autowired
    private UserAccountsRepository userAccountsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    public void beforeEach() {
        cleaner.deleteUsersFromDB();
        when(keycloakUsersService.create(any())).thenReturn(testUserId);
        userAccountsService.createFromDto(testUserRequest);
    }

    @Test
    void testUpdateDisplayName() {
        String newDisplayName = "newDisplayName";
        User user = usersService.getById(testUserId);

        assertEquals(UsersService.UNNAMED, user.getDisplayName());

        usersService.updateDisplayName(testUserId, newDisplayName);
        user = usersService.getById(testUserId);

        assertEquals(newDisplayName, user.getDisplayName());
    }

    @org.junit.jupiter.params.ParameterizedTest
    @MethodSource(value = "provideDisplayNames")
    void testUpdateNullDisplayName(String input) {
        User user = usersService.getById(testUserId);

        assertEquals(UsersService.UNNAMED, user.getDisplayName());

        assertThrows(ServiceException.class, () -> usersService.updateDisplayName(testUserId, input));
    }

    @Test
    void testUpdateRoleName() {
        String newRoleName = "newRoleName";
        User user = usersService.getById(testUserId);
        assertNull(user.getRoleName());

        usersService.updateRoleName(testUserId, newRoleName);

        user = usersService.getById(testUserId);

        assertEquals(newRoleName, user.getRoleName());
    }

    @Test
    void testUpdatePersonalData() {
        String newPersonalDataInfo = "testInfo";
        User user = usersService.getById(testUserId);
        PersonalData personalData = user.getPersonalData();

        assertNotNull(personalData);
        assertNull(personalData.getFirstName());
        assertNull(personalData.getLastName());
        assertNull(personalData.getMiddleName());
        assertNull(personalData.getInfo());
        assertNotNull(personalData.getContacts());
        assertThat(personalData.getContacts().isEmpty()).isTrue();


        personalData.setFirstName(newPersonalDataInfo);
        personalData.setLastName(newPersonalDataInfo);
        personalData.setMiddleName(newPersonalDataInfo);
        personalData.setInfo(newPersonalDataInfo);
        usersService.updatePersonalData(testUserId, personalData);

        PersonalData updatedData = usersService.getById(testUserId).getPersonalData();
        assertNotNull(updatedData);
        assertEquals(newPersonalDataInfo, updatedData.getFirstName());
        assertEquals(newPersonalDataInfo, updatedData.getLastName());
        assertEquals(newPersonalDataInfo, updatedData.getMiddleName());
        assertEquals(newPersonalDataInfo, updatedData.getInfo());
        assertNotNull(user.getPersonalData().getContacts());
        assertThat(user.getPersonalData().getContacts().isEmpty()).isTrue();
    }

    @Test
    void testUpdateContacts() {
        String testContactInfo = "testContactInfo";
        Contact newContact = new Contact();
        newContact.setInfo(testContactInfo);
        newContact.setDescription(testContactInfo);
        newContact.setType(testContactInfo);

        User user = usersService.getById(testUserId);
        assertNotNull(user);

        List<Contact> contactList = usersService.getContactsById(testUserId);
        assertNotNull(contactList);
        assertTrue(contactList.isEmpty());

        contactList.add(newContact);
        usersService.updateContacts(testUserId, contactList);

        List<Contact> updatedContactList = usersService.getContactsById(testUserId);
        assertNotNull(contactList);
        assertEquals(1, updatedContactList.size());
        assertEquals(newContact, updatedContactList.get(0));

        usersService.updateContacts(testUserId, Collections.emptyList());

        List<Contact> clearedList = usersService.getContactsById(testUserId);
        assertNotNull(contactList);
        assertTrue(clearedList.isEmpty());
    }

    @Test
    void testCreateUserWithoutAccount(){
        User user = new User();
        user.setDisplayName("testName");

        assertThrows(JpaSystemException.class, ()->usersRepository.save(user));
    }


    private static Stream<Arguments> provideDisplayNames() {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of("  ")
        );
    }
}