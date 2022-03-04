package org.h0110w.som.core.repository;

import org.h0110w.som.core.model.tasks.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TasksRepository extends CrudRepository<Task, Long> {
    Set<Task> findByResponsibleId(UUID userId);

    @Query(value = "SELECT DISTINCT t FROM Task t LEFT JOIN t.watchers u " +
            "WHERE u.id = :userId")
    Set<Task> findByWatcherId(@Param("userId") UUID userId);

    Set<Task> findBySuperTaskId(Long taskId);
}
