package com.sevval.PlanoraTaskPlannerBackend.repository;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.Pomodoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PomodoroRepository extends JpaRepository<Pomodoro, Long> {
}

