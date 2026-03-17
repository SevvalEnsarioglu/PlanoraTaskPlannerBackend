package com.sevval.PlanoraTaskPlannerBackend.mapper;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.CategoryRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.CategoryResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDTO request);
    CategoryResponseDTO toResponseDTO(Category category);
}
