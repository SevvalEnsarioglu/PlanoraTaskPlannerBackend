package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.mapper.TaskMapper;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TaskRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TaskResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Category;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Priority;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Tag;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Task;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import com.sevval.PlanoraTaskPlannerBackend.repository.CategoryRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.PriorityRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.TagRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.TaskRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.UserRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil;
import com.sevval.PlanoraTaskPlannerBackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PriorityRepository priorityRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> listTasks(Long userId, LocalDate date, Boolean completed) {
        enforceCurrentUser(userId);
        List<Task> tasks;

        if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);
            tasks = taskRepository.findAllByUserIdAndDueDateBetween(userId, start, end);
        } else if (completed != null) {
            tasks = taskRepository.findAllByUserIdAndIsCompleted(userId, completed);
        } else {
            tasks = taskRepository.findAllByUserId(userId);
        }

        return tasks.stream().map(taskMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO getTask(Long userId, Long taskId) {
        enforceCurrentUser(userId);
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        return taskMapper.toResponseDTO(task);
    }

    @Override
    public TaskResponseDTO createTask(Long userId, TaskRequestDTO request) {
        enforceCurrentUser(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task task = taskMapper.toEntity(request);
        task.setUser(user);
        task.setTaskNo(generateTaskNo());

        applyRelations(userId, request, task);

        Task saved = taskRepository.save(task);
        return taskMapper.toResponseDTO(saved);
    }

    @Override
    public TaskResponseDTO updateTask(Long userId, Long taskId, TaskRequestDTO request) {
        enforceCurrentUser(userId);
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        if (request.isCompleted() != null) {
            task.setIsCompleted(request.isCompleted());
        }

        applyRelations(userId, request, task);

        return taskMapper.toResponseDTO(task);
    }

    @Override
    public TaskResponseDTO setCompleted(Long userId, Long taskId, boolean completed) {
        enforceCurrentUser(userId);
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        task.setIsCompleted(completed);
        return taskMapper.toResponseDTO(task);
    }

    @Override
    public void deleteTask(Long userId, Long taskId) {
        enforceCurrentUser(userId);
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    private void applyRelations(Long userId, TaskRequestDTO request, Task task) {
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findByIdAndUserId(request.categoryId(), userId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        }
        task.setCategory(category);

        Tag tag = null;
        if (request.tagId() != null) {
            tag = tagRepository.findByIdAndUserId(request.tagId(), userId)
                    .orElseThrow(() -> new NotFoundException("Tag not found"));
        }
        task.setTag(tag);

        Priority priority = null;
        if (request.priorityId() != null) {
            priority = priorityRepository.findByIdAndUserId(request.priorityId(), userId)
                    .orElseThrow(() -> new NotFoundException("Priority not found"));
        }
        task.setPriority(priority);
    }

    private static String generateTaskNo() {
        return "TSK-" + System.currentTimeMillis();
    }

    private static void enforceCurrentUser(Long userId) {
        Long current = SecurityUtil.currentUserIdOrNull();
        if (current != null && !current.equals(userId)) {
            throw new NotFoundException("Task not found");
        }
    }
}
