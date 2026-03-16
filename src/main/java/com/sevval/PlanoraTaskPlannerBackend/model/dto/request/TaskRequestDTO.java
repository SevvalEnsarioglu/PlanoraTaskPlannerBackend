package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

import java.time.LocalDateTime;

public record TaskRequestDTO(
        String title,
        String description,
        LocalDateTime dueDate,
        Boolean isCompleted,
        Long categoryId,
        Long tagId,
        Long priorityId
) {
}
