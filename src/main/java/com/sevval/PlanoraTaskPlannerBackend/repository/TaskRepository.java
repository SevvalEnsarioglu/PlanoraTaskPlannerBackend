package com.sevval.PlanoraTaskPlannerBackend.repository;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long userId);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    List<Task> findAllByUserIdAndDueDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<Task> findAllByUserIdAndIsCompleted(Long userId, Boolean isCompleted);
    List<Task> findAllByUserIdAndDueDateAfter(Long userId, LocalDateTime date);

    long countByUserIdAndIsCompletedTrue(Long userId);
    long countByUserIdAndIsCompletedFalse(Long userId);
    long countByUserIdAndIsCompletedTrueAndDueDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT c.name, COUNT(t) FROM Task t JOIN t.category c WHERE t.user.id = :userId AND t.isCompleted = true GROUP BY c.name")
    List<Object[]> countCompletedTasksByCategory(@Param("userId") Long userId);

    // Haftalık üretkenlik: Bu haftaki tamamlanan görevleri getir (updatedAt = tamamlanma zamanı)
    List<Task> findAllByUserIdAndIsCompletedTrueAndUpdatedAtBetween(Long userId, Instant start, Instant end);

    // Streak hesabı: tüm zamanlar tamamlanan görevler (updatedAt bazlı)
    List<Task> findAllByUserIdAndIsCompletedTrueOrderByUpdatedAtDesc(Long userId);
}

