package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

import java.time.LocalDateTime;

public record PomodoroSessionRequestDTO(
        Integer durationInMinutes,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long taskId
) {
}
