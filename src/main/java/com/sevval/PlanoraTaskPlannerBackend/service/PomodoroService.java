package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PomodoroRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PomodoroResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface PomodoroService {
    PomodoroResponseDTO create(Long userId, PomodoroRequestDTO request);
    List<PomodoroResponseDTO> list(Long userId, Long taskId, LocalDateTime from, LocalDateTime to);
    void delete(Long userId, Long pomodoroId);
}


