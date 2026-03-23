package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.BadRequestException;
import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.mapper.UserMapper;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.LoginRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.RegisterRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.LoginResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.UserResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import com.sevval.PlanoraTaskPlannerBackend.repository.UserRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.JwtService;
import com.sevval.PlanoraTaskPlannerBackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        // JWT subject = userId
        String token = jwtService.generateToken(user.getId());
        return new LoginResponseDTO(token, user.getUsername(), "Login successful");
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUser() {
        Long userId = com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil.currentUserIdOrNull();
        if (userId == null) {
            throw new NotFoundException("User not authenticated");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toResponseDTO(user);
    }
}


