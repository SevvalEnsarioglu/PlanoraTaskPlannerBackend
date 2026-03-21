package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.StatisticsResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.repository.PomodoroRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.TaskRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil;
import com.sevval.PlanoraTaskPlannerBackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

    private final TaskRepository taskRepository;
    private final PomodoroRepository pomodoroRepository;

    @Override
    public StatisticsResponseDTO getUserStatistics(Long userId) {
        enforceCurrentUser(userId);
        
        long totalCompleted = taskRepository.countByUserIdAndIsCompletedTrue(userId);

        LocalDateTime startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(7).minusNanos(1);
        long weeklyCompleted = taskRepository.countByUserIdAndIsCompletedTrueAndDueDateBetween(userId, startOfWeek, endOfWeek);

        List<Object[]> categoryCounts = taskRepository.countCompletedTasksByCategory(userId);
        Map<String, Long> categoryDistribution = new HashMap<>();
        for (Object[] result : categoryCounts) {
            String categoryName = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            categoryDistribution.put(categoryName != null ? categoryName : "Uncategorized", count);
        }

        Long totalPomodoro = pomodoroRepository.sumDurationByUserId(userId);
        long pomodoroMinutes = (totalPomodoro != null) ? totalPomodoro : 0L;

        return new StatisticsResponseDTO(totalCompleted, weeklyCompleted, categoryDistribution, pomodoroMinutes);
    }

    private void enforceCurrentUser(Long userId) {
        Long current = SecurityUtil.currentUserIdOrNull();
        if (current != null && !current.equals(userId)) {
            throw new NotFoundException("User not found or access denied");
        }
    }
}
