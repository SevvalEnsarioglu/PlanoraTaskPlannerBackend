package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.mapper.PriorityMapper;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PriorityRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PriorityResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Priority;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import com.sevval.PlanoraTaskPlannerBackend.repository.PriorityRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.UserRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil;
import com.sevval.PlanoraTaskPlannerBackend.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;
    private final UserRepository userRepository;
    private final PriorityMapper priorityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PriorityResponseDTO> list(Long userId) {
        enforceCurrentUser(userId);
        return priorityRepository.findAllByUserId(userId).stream().map(priorityMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PriorityResponseDTO get(Long userId, Long priorityId) {
        enforceCurrentUser(userId);
        Priority priority = priorityRepository.findByIdAndUserId(priorityId, userId)
                .orElseThrow(() -> new NotFoundException("Priority not found"));
        return priorityMapper.toResponseDTO(priority);
    }

    @Override
    public PriorityResponseDTO create(Long userId, PriorityRequestDTO request) {
        enforceCurrentUser(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Priority priority = priorityMapper.toEntity(request);
        priority.setUser(user);
        Priority saved = priorityRepository.save(priority);
        return priorityMapper.toResponseDTO(saved);
    }

    @Override
    public PriorityResponseDTO update(Long userId, Long priorityId, PriorityRequestDTO request) {
        enforceCurrentUser(userId);
        Priority priority = priorityRepository.findByIdAndUserId(priorityId, userId)
                .orElseThrow(() -> new NotFoundException("Priority not found"));
        priority.setName(request.name());
        priority.setColorCode(request.colorCode());
        priority.setLevel(request.level());
        return priorityMapper.toResponseDTO(priority);
    }

    @Override
    public void delete(Long userId, Long priorityId) {
        enforceCurrentUser(userId);
        Priority priority = priorityRepository.findByIdAndUserId(priorityId, userId)
                .orElseThrow(() -> new NotFoundException("Priority not found"));
        priorityRepository.delete(priority);
    }

    private static void enforceCurrentUser(Long userId) {
        Long current = SecurityUtil.currentUserIdOrNull();
        if (current != null && !current.equals(userId)) {
            throw new NotFoundException("Priority not found");
        }
    }
}


