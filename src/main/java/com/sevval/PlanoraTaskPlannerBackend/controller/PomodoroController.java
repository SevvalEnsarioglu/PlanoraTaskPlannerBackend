package com.sevval.PlanoraTaskPlannerBackend.controller;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PomodoroRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PomodoroResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.service.PomodoroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/pomodoros")
@RequiredArgsConstructor
public class PomodoroController {

    private final PomodoroService pomodoroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PomodoroResponseDTO create(@PathVariable Long userId, @RequestBody @Valid PomodoroRequestDTO request) {
        return pomodoroService.create(userId, request);
    }

    @GetMapping
    public List<PomodoroResponseDTO> list(
            @PathVariable Long userId,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return pomodoroService.list(userId, taskId, from, to);
    }

    @DeleteMapping("/{pomodoroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long pomodoroId) {
        pomodoroService.delete(userId, pomodoroId);
    }
}


