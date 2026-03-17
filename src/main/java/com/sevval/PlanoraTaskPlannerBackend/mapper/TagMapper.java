package com.sevval.PlanoraTaskPlannerBackend.mapper;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TagRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TagResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toEntity(TagRequestDTO request);
    TagResponseDTO toResponseDTO(Tag tag);
}
