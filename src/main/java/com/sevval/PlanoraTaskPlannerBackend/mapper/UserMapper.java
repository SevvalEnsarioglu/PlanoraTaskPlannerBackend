package com.sevval.PlanoraTaskPlannerBackend.mapper;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.RegisterRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.UserRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.UserResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO request);
    User toEntity(RegisterRequestDTO request);
    UserResponseDTO toResponseDTO(User user);
}
