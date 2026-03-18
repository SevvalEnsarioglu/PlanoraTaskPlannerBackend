package com.sevval.PlanoraTaskPlannerBackend.repository;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.Pomodoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PomodoroRepository extends JpaRepository<Pomodoro, Long> {
    List<Pomodoro> findAllByTaskId(Long taskId);
    List<Pomodoro> findAllByTaskUserId(Long userId);
    List<Pomodoro> findAllByTaskUserIdAndStartTimeBetween(Long userId, LocalDateTime from, LocalDateTime to);
}

