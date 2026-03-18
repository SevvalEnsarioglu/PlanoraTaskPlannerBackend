package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TagRequestDTO(
        @NotBlank String name,
        String colorCode
) {
}
