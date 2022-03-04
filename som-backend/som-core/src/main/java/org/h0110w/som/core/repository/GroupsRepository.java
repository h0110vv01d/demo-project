package org.h0110w.som.core.repository;

import org.h0110w.som.core.model.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupsRepository extends CrudRepository<Group, UUID> {
}
