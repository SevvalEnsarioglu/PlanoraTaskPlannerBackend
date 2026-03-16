package com.sevval.PlanoraTaskPlannerBackend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record TagResponseDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String colorCode
) {
}
