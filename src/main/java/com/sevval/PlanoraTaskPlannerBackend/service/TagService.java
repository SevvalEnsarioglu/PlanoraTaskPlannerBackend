package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TagRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TagResponseDTO;

import java.util.List;

public interface TagService {
    List<TagResponseDTO> list(Long userId);
    TagResponseDTO get(Long userId, Long tagId);
    TagResponseDTO create(Long userId, TagRequestDTO request);
    TagResponseDTO update(Long userId, Long tagId, TagRequestDTO request);
    void delete(Long userId, Long tagId);
}


