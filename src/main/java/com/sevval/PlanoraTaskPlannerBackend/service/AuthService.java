package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.LoginRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.RegisterRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.LoginResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.UserResponseDTO;

public interface AuthService {
    UserResponseDTO register(RegisterRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);
    UserResponseDTO getCurrentUser();
}


