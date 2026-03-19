package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PriorityRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PriorityResponseDTO;

import java.util.List;

public interface PriorityService {
    List<PriorityResponseDTO> list(Long userId);
    PriorityResponseDTO get(Long userId, Long priorityId);
    PriorityResponseDTO create(Long userId, PriorityRequestDTO request);
    PriorityResponseDTO update(Long userId, Long priorityId, PriorityRequestDTO request);
    void delete(Long userId, Long priorityId);
}


