package com.sevval.PlanoraTaskPlannerBackend.controller;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.LoginRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.RegisterRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.LoginResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.UserResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid RegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
        return authService.login(request);
    }
}


