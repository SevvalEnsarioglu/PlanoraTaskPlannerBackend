package com.sevval.PlanoraTaskPlannerBackend.repository;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long userId);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    List<Task> findAllByUserIdAndDueDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<Task> findAllByUserIdAndIsCompleted(Long userId, Boolean isCompleted);

    long countByUserIdAndIsCompletedTrue(Long userId);
    long countByUserIdAndIsCompletedTrueAndDueDateBetween(Long userId, java.time.LocalDateTime start, java.time.LocalDateTime end);

    @org.springframework.data.jpa.repository.Query("SELECT c.name, COUNT(t) FROM Task t JOIN t.category c WHERE t.user.id = :userId AND t.isCompleted = true GROUP BY c.name")
    java.util.List<Object[]> countCompletedTasksByCategory(@org.springframework.data.repository.query.Param("userId") Long userId);
}
