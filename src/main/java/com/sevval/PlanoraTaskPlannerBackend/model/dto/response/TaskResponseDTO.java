package com.sevval.PlanoraTaskPlannerBackend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDateTime;

public record TaskResponseDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String description,
        LocalDateTime dueDate,
        Boolean isCompleted,
        CategoryResponseDTO category,
        TagResponseDTO tag,
        PriorityResponseDTO priority
) {
}
