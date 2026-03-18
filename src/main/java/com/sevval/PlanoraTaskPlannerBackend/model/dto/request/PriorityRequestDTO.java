package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PriorityRequestDTO(
        @NotBlank String name,
        String colorCode,
        @NotNull Integer level
) {
}
