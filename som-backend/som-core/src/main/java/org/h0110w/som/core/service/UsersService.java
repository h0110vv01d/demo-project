package org.h0110w.som.core.service;

import org.apache.commons.lang3.StringUtils;
import org.h0110w.som.core.configuration.messages.CustomMessageSource;
import org.h0110w.som.core.repository.UsersRepository;
import org.h0110w.som.core.exception.ServiceException;
import org.h0110w.som.core.model.user.Contact;
import org.h0110w.som.core.model.user.PersonalData;
import org.h0110w.som.core.model.user.User;
import org.h0110w.som.core.model.user_account.UserAccount;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class UsersService {
    public static final String UNNAMED = "unnamed";
    private final UsersRepository usersRepository;
    private final CustomMessageSource messageSource;

    @Autowired
    public UsersService(UsersRepository usersRepository, CustomMessageSource messageSource) {
        this.usersRepository = usersRepository;
        this.messageSource = messageSource;
    }


    @Transactional
    public void create(UserAccount account) {
        User user = new User(account);

        usersRepository.save(user);
    }

    @Transactional
    public void updateDisplayName(UUID id, String displayName) {
        User user = getById(id);

        if (StringUtils.isBlank(displayName)) {
            throw new ServiceException(messageSource.get("display.name.is.null"));
        }
        user.setDisplayName(displayName);

        usersRepository.save(user);
    }

    @Transactional
    public void updateRoleName(UUID userId, String newRoleName) {
        User user = getById(userId);
        if (!isRoleNameValid(newRoleName)) {
            throw new ServiceException(messageSource.get("rolename.invalid"));
        }
        user.setRoleName(newRoleName);

        usersRepository.save(user);
    }

    @Transactional
    public void updatePersonalData(UUID userId, PersonalData personalData) {
        User user = getById(userId);

        PersonalData newData = new PersonalData();
        newData.setFirstName(personalData.getFirstName());
        newData.setLastName(personalData.getLastName());
        newData.setMiddleName(personalData.getMiddleName());
        newData.setInfo(personalData.getInfo());

        user.setPersonalData(newData);

        usersRepository.save(user);
    }

    @Transactional
    public void updateContacts(UUID userId, List<Contact> contactList) {
        User user = getById(userId);

        user.getPersonalData().setContacts(contactList);

        usersRepository.save(user);
    }

    @Transactional
    public List<Contact> getContactsById(UUID userId) {
        User user = getById(userId);
        Hibernate.initialize(user.getPersonalData().getContacts());
        return (List<Contact>) user.getPersonalData().getContacts();
    }

    private boolean isRoleNameValid(String roleName) {
        return true;
    }

    public User getById(UUID userId) {
        if (userId == null) {
            throw new ServiceException(messageSource.get("id.is.null"));
        }
        return usersRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(messageSource.get("user.not.found")));
    }

    @Transactional
    public void leaveGroup(UUID id) {
        User user = getById(id);
        if (user.getGroup() != null) {
            user.getGroup().getUsers().remove(user);
            user.setGroup(null);
        }
        if (user.getControlledGroup() != null) {
            user.getControlledGroup().setLead(null);
            user.setControlledGroup(null);
        }
        usersRepository.save(user);
    }

//    @Transactional
//    public void detachTasks(UUID id) {
//        User user = getById(id);
//
//        user.getWatchedTasks().forEach(task -> task.getWatchers().remove(user));
//        user.getWatchedTasks().clear();
//
//        user.getTasks().forEach(task -> task.setResponsible(null));
//        user.getTasks().clear();
//
//        tasksService.getTasksAssginedOnUser(id).forEach(task -> task.setCreatedBy(null));
//
//        usersRepository.save(user);
//    }
}
