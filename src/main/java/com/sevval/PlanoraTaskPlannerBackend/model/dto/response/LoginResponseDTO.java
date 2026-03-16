package com.sevval.PlanoraTaskPlannerBackend.model.dto.response;

public record LoginResponseDTO(
        String token,
        String username,
        String message
) {
}
