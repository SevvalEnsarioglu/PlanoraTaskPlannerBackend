package com.sevval.PlanoraTaskPlannerBackend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDateTime;

public record PomodoroSessionResponseDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        Instant createdAt,
        Instant updatedAt,
        Integer durationInMinutes,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long taskId
) {
}
