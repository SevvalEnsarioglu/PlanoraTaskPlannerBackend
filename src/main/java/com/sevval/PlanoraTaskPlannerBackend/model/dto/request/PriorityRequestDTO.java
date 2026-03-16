package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

public record PriorityRequestDTO(
        String name,
        String colorCode,
        Integer level
) {
}
