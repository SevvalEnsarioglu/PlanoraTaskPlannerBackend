package com.sevval.PlanoraTaskPlannerBackend.mapper;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PriorityRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PriorityResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Priority;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriorityMapper {
    Priority toEntity(PriorityRequestDTO request);
    PriorityResponseDTO toResponseDTO(Priority priority);
}
