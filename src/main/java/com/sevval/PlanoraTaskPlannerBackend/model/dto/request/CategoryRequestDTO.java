package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank String name,
        String colorCode
) {
}
