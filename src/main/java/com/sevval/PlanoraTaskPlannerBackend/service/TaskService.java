package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TaskRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TaskResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    List<TaskResponseDTO> listTasks(Long userId, LocalDate date, LocalDate startDate, LocalDate endDate, Boolean completed);
    TaskResponseDTO getTask(Long userId, Long taskId);
    TaskResponseDTO createTask(Long userId, TaskRequestDTO request);
    TaskResponseDTO updateTask(Long userId, Long taskId, TaskRequestDTO request);
    TaskResponseDTO setCompleted(Long userId, Long taskId, boolean completed);
    void deleteTask(Long userId, Long taskId);
}
