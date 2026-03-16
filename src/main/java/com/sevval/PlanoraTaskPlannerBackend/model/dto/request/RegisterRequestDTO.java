package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

public record RegisterRequestDTO(
        String username,
        String name,
        String surname,
        String email,
        String phoneNumber,
        String password
) {
}
