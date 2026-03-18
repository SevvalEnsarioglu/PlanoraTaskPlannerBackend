package com.sevval.PlanoraTaskPlannerBackend.controller;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TaskRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TaskResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskResponseDTO> list(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Boolean completed
    ) {
        return taskService.listTasks(userId, date, completed);
    }

    @GetMapping("/{taskId}")
    public TaskResponseDTO get(@PathVariable Long userId, @PathVariable Long taskId) {
        return taskService.getTask(userId, taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO create(@PathVariable Long userId, @RequestBody @Valid TaskRequestDTO request) {
        return taskService.createTask(userId, request);
    }

    @PutMapping("/{taskId}")
    public TaskResponseDTO update(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @RequestBody @Valid TaskRequestDTO request
    ) {
        return taskService.updateTask(userId, taskId, request);
    }

    @PatchMapping("/{taskId}/complete")
    public TaskResponseDTO setCompleted(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @RequestParam boolean completed
    ) {
        return taskService.setCompleted(userId, taskId, completed);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long taskId) {
        taskService.deleteTask(userId, taskId);
    }
}
