package org.h0110w.som.core.service;


import lombok.extern.slf4j.Slf4j;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.controller.request.UpdateUserRequestDto;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.repository.UserAccountsRepository;
import org.h0110w.som.core.service.mapper.Mapper;
import org.h0110w.som.core.service.util.pagination.CustomPageRequest;
import org.h0110w.som.core.service.util.pagination.PagedResult;
import org.h0110w.som.core.model.user_account.UserAccount;
import org.h0110w.som.core.model.user_account.UserAccountDto;
import org.h0110w.som.core.model.user_account.UserAccountType;
import org.h0110w.som.core.model.user_account.UserAccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Users management service
 */
@Slf4j
@Service
public class UserAccountsService {
    private static final long MIN_PASSWORD_LENGTH = 6;

    private final UserAccountsRepository userAccountsRepository;

    private final KeycloakUsersService keycloakUsersService;

    private final UsersService usersService;

    private final CustomMessageSource messageSource;

    @Autowired
    public UserAccountsService(UserAccountsRepository userAccountsRepository, KeycloakUsersService keycloakUsersService, UsersService usersService, CustomMessageSource messageSource) {
        this.userAccountsRepository = userAccountsRepository;
        this.keycloakUsersService = keycloakUsersService;
        this.usersService = usersService;
        this.messageSource = messageSource;
    }


    /**
     * processes CreateUserRequestDto
     * validates password and login, checks for existing user duplicates
     * creates user in keycloak realm and receives his id
     * sets additional info and saves User entity in database
     *
     * @param request dto that contains user data
     * @return userDto contains info about created user
     */
    @Transactional
    public UserAccountDto createFromDto(CreateUserAccountRequestDto request) {
        checkUserLogin(request.getLogin());
        checkPassword(request.getPassword());

        UserAccount userAccount = new UserAccount();

        UUID id = keycloakUsersService.create(request);

        userAccount.setId(id);
        userAccount.setLogin(request.getLogin());
        userAccount.setUserAccountType(request.getUserRole());
        userAccount.setUserAccountStatus(UserAccountStatus.ACTIVE);
        userAccount.setBuiltIn(false);

        userAccount.setUser(new User(userAccount));

        userAccount = userAccountsRepository.save(userAccount);

        return Mapper.USER_ACCOUNT.toDto(userAccount);
    }


    @Transactional
    public UserAccountDto updateByDto(UpdateUserRequestDto updateUserRequestDto) {
        UserAccount updatedUserAccount = update(updateUserRequestDto);
        return Mapper.USER_ACCOUNT.toDto(updatedUserAccount);
    }

    @Transactional
    public UserAccount update(UpdateUserRequestDto userDto) {
        boolean isUserUpdated = false;
        UserAccount userAccount = getById(userDto.getId());

        String password = userDto.getPassword();
        if (!StringUtils.isEmpty(password)) {
            checkPassword(password);
            keycloakUsersService.updatePassword(userAccount.getId().toString(), password);
            isUserUpdated = true;
        }

        final boolean areRolesUpdated = userDto.getUserRole() != null &&
                !userAccount.getUserAccountType().equals(userDto.getUserRole());
        if (areRolesUpdated) {
            userAccount.setUserAccountType(userDto.getUserRole());
            keycloakUsersService.assignRoleToUser(userDto.getUserRole(), userAccount.getId().toString());
            isUserUpdated = true;
        }

        if (isUserUpdated) {
            userAccount = userAccountsRepository.save(userAccount);
        }

        return userAccount;
    }

    public UserAccount getById(UUID id) {
        if (id == null) {
            throw new ServiceException(messageSource.get("id.is.null"));
        }
        return userAccountsRepository.findById(id)
                .orElseThrow(() -> new ServiceException(messageSource.get("user.not.found")));
    }

    public UserAccount getByLogin(String login) {
        if (StringUtils.isEmpty(login)) {
            throw new ServiceException(messageSource.get("login.is.null"));
        }
        return userAccountsRepository.findByLogin(login)
                .orElseThrow(() -> new ServiceException(messageSource.get("user.not.found")));
    }

    @Transactional
    public void disable(UUID userId) {
        UserAccount userAccount = getById(userId);
        if (userAccount.getUserAccountStatus().equals(UserAccountStatus.DISABLED)) {
            throw new ServiceException(messageSource.get("user.disabled"));
        }
        keycloakUsersService.disable(userId);

        userAccount.setUserAccountStatus(UserAccountStatus.DISABLED);
        userAccountsRepository.save(userAccount);
    }

    public void enable(UUID userId) {
        UserAccount userAccount = getById(userId);
        if (userAccount.getUserAccountStatus().equals(UserAccountStatus.ACTIVE)) {
            throw new ServiceException(messageSource.get("user.enabled"));
        }
        keycloakUsersService.enable(userId.toString());

        userAccount.setUserAccountStatus(UserAccountStatus.DISABLED);
        userAccountsRepository.save(userAccount);
    }

    @Transactional
    public void delete(UUID id) {
        UserAccount userAccount = getById(id);
        if (userAccount.isBuiltIn()) {
            log.warn("Cannot delete builtin user");
            throw new IllegalArgumentException(messageSource.get("cannot.delete.builtin.user"));
        }

        usersService.leaveGroup(id);

        userAccountsRepository.deleteById(id);
    }

    public PagedResult<UserAccountDto> paged(CustomPageRequest pageRequest) {
        Specification<UserAccount> spec = pageRequest.getSpecification();
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userAccountType"), UserAccountType.REGULAR));

        Page<UserAccount> page = userAccountsRepository.findAll(spec, pageRequest.getPageable());

        return PagedResult.of(page, Mapper.USER_ACCOUNT.toDtos(page.getContent()));
    }

    public long getUserCount() {
        return userAccountsRepository.count();
    }

    private void checkUserLogin(String userLogin) {
        boolean isUserWithSameLoginExists = userAccountsRepository.findByLogin(userLogin).isPresent();
        if (isUserWithSameLoginExists) {
            log.warn("Cannot create user with login " + userLogin + " - it already exists");
            throw new ServiceException(messageSource.get("user.login.already.exists", userLogin));
        }
    }

    private void checkPassword(String password) {
        boolean isPasswordInvalid = true;
        if (!StringUtils.isEmpty(password)) {
            isPasswordInvalid = password.length() < MIN_PASSWORD_LENGTH;
        }
        if (isPasswordInvalid) {
            log.warn("Cannot create/update user: invalid password");
            throw new ServiceException(messageSource.get("invalid.password"));
        }
    }
}
