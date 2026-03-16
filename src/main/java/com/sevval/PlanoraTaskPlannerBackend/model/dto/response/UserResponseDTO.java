package com.sevval.PlanoraTaskPlannerBackend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record UserResponseDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String name,
        String surname,
        String email,
        String phoneNumber
) {
}
