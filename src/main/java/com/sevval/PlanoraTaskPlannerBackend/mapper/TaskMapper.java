package com.sevval.PlanoraTaskPlannerBackend.mapper;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TaskRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TaskResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskRequestDTO request);
    @org.mapstruct.Mapping(target = "pomodoroCount", expression = "java(task.getPomodoros() != null ? task.getPomodoros().size() : 0)")
    TaskResponseDTO toResponseDTO(Task task);
}
