package com.sevval.PlanoraTaskPlannerBackend.model.dto.request;

public record UserRequestDTO(
        String name,
        String surname,
        String phoneNumber
) {
}
