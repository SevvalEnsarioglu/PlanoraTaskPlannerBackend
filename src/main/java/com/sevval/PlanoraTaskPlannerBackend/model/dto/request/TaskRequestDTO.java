package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record TaskRequestDTO(
        @NotBlank String title,
        String description,
        LocalDateTime dueDate,
        Boolean isCompleted,
        Long categoryId,
        java.util.List<Long> tagIds,
        Long priorityId
) {
}
