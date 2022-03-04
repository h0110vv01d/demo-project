package org.h0110w.som.core.repository;

import org.h0110w.som.core.model.user_account.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountsRepository extends JpaRepository<UserAccount, UUID>,
        JpaSpecificationExecutor<UserAccount> {
    Optional<UserAccount> findByLogin(String login);
}
