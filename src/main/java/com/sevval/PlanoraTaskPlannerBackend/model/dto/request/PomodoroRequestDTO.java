package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PomodoroRequestDTO(
        @NotNull Integer durationInMinutes,
        LocalDateTime startTime,
        LocalDateTime endTime,
        @NotNull Long taskId
) {
}
