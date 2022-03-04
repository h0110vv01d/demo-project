package org.h0110w.som.core.configuration.security;

import org.h0110w.som.core.configuration.CustomProfiles;
import org.h0110w.som.core.configuration.security.keycloak.KeycloakConfig;
import org.h0110w.som.core.repository.UsersRepository;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.model.user_account.UserAccount;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.h0110w.som.core.model.user_account.UserAccountStatus;
import org.h0110w.som.core.repository.UserAccountsRepository;
import org.h0110w.som.core.service.KeycloakUsersService;
import org.h0110w.som.core.service.UserAccountsService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Profile(CustomProfiles.DEFAULT_DB)
public class AdminInitializer implements InitializingBean {
    private final KeycloakConfig keycloakConfig;

    private final KeycloakUsersService keycloakUsersService;

    private final UserAccountsService userAccountsService;

    private final UserAccountsRepository userAccountsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public AdminInitializer(KeycloakConfig keycloakConfig, KeycloakUsersService keycloakUsersService, UserAccountsService userAccountsService, UserAccountsRepository userAccountsRepository, UsersRepository usersRepository) {
        this.keycloakConfig = keycloakConfig;
        this.keycloakUsersService = keycloakUsersService;
        this.userAccountsService = userAccountsService;
        this.userAccountsRepository = userAccountsRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public void afterPropertiesSet(){
        if (!doesKeycloakAdminExist()) {
            keycloakUsersService.createKeycloakAdmin();
        }
        if (!doesAdminAccountExist()) {
            createAdminAccount();
        }
    }

    private boolean doesKeycloakAdminExist() {
        try {
            keycloakUsersService.getAdminId();
            return true;
        } catch (BeanInitializationException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    void createAdminAccount() {
        UUID id = keycloakUsersService.getAdminId();
        if (id==null){
            throw new BeanInitializationException("keycloak admin is not created");
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setId(id);
        userAccount.setLogin(keycloakConfig.getSomRealmAdminUsername());
        userAccount.setUserAccountType(UserAccountType.ADMIN);
        userAccount.setUserAccountStatus(UserAccountStatus.ACTIVE);
        userAccount.setBuiltIn(true);

        userAccountsRepository.save(userAccount);

        User user = new User();
        user.setDisplayName(userAccount.getLogin());
        user.setAccount(userAccount);

        usersRepository.save(user);
    }

    private boolean doesAdminAccountExist() {
        try {
            userAccountsService.getByLogin(keycloakConfig.getSomRealmAdminUsername());
            return true;
        } catch (ServiceException e) {
            //ignore
        }
        return false;
    }

}
