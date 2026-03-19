package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.mapper.PomodoroMapper;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PomodoroRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PomodoroResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Pomodoro;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Task;
import com.sevval.PlanoraTaskPlannerBackend.repository.PomodoroRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.TaskRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil;
import com.sevval.PlanoraTaskPlannerBackend.service.PomodoroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PomodoroServiceImpl implements PomodoroService {

    private final PomodoroRepository pomodoroRepository;
    private final TaskRepository taskRepository;
    private final PomodoroMapper pomodoroMapper;

    @Override
    public PomodoroResponseDTO create(Long userId, PomodoroRequestDTO request) {
        enforceCurrentUser(userId);
        if (request.taskId() == null) {
            throw new NotFoundException("Task not found");
        }

        Task task = taskRepository.findByIdAndUserId(request.taskId(), userId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        Pomodoro pomodoro = pomodoroMapper.toEntity(request);
        pomodoro.setTask(task);

        Pomodoro saved = pomodoroRepository.save(pomodoro);
        return pomodoroMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PomodoroResponseDTO> list(Long userId, Long taskId, LocalDateTime from, LocalDateTime to) {
        enforceCurrentUser(userId);
        List<Pomodoro> pomodoros;

        if (taskId != null) {
            // Ownership check via Task
            taskRepository.findByIdAndUserId(taskId, userId)
                    .orElseThrow(() -> new NotFoundException("Task not found"));
            pomodoros = pomodoroRepository.findAllByTaskId(taskId);
        } else if (from != null && to != null) {
            pomodoros = pomodoroRepository.findAllByTaskUserIdAndStartTimeBetween(userId, from, to);
        } else {
            pomodoros = pomodoroRepository.findAllByTaskUserId(userId);
        }

        return pomodoros.stream().map(pomodoroMapper::toResponseDTO).toList();
    }

    @Override
    public void delete(Long userId, Long pomodoroId) {
        enforceCurrentUser(userId);
        Pomodoro pomodoro = pomodoroRepository.findById(pomodoroId)
                .orElseThrow(() -> new NotFoundException("Pomodoro not found"));
        if (!pomodoro.getTask().getUser().getId().equals(userId)) {
            throw new NotFoundException("Pomodoro not found");
        }
        pomodoroRepository.delete(pomodoro);
    }

    private static void enforceCurrentUser(Long userId) {
        Long current = SecurityUtil.currentUserIdOrNull();
        if (current != null && !current.equals(userId)) {
            throw new NotFoundException("Pomodoro not found");
        }
    }
}


