package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.CategoryRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> list(Long userId);
    CategoryResponseDTO get(Long userId, Long categoryId);
    CategoryResponseDTO create(Long userId, CategoryRequestDTO request);
    CategoryResponseDTO update(Long userId, Long categoryId, CategoryRequestDTO request);
    void delete(Long userId, Long categoryId);
}


