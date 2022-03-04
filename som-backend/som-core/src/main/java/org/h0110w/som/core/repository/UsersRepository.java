package org.h0110w.som.core.repository;

import org.h0110w.som.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
//    List<Contact> findByAccountId(String id);
}
