package com.sevval.PlanoraTaskPlannerBackend.mapper;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PomodoroRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PomodoroResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Pomodoro;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface PomodoroMapper {
    Pomodoro toEntity(PomodoroRequestDTO request);
    PomodoroResponseDTO toResponseDTO(Pomodoro pomodoro);
}
