package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.HeatmapResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.StatisticsResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Task;
import com.sevval.PlanoraTaskPlannerBackend.repository.PomodoroRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.TaskRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil;
import com.sevval.PlanoraTaskPlannerBackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

    private final TaskRepository taskRepository;
    private final PomodoroRepository pomodoroRepository;

    private static final String[] TR_DAYS = {"Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz"};

    @Override
    public StatisticsResponseDTO getUserStatistics(Long userId) {
        enforceCurrentUser(userId);

        ZoneId zone = ZoneId.systemDefault();

        long totalCompleted = taskRepository.countByUserIdAndIsCompletedTrue(userId);

        LocalDateTime startOfWeekLdt = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        LocalDateTime endOfWeekLdt = startOfWeekLdt.plusDays(7).minusNanos(1);
        long weeklyCompleted = taskRepository
                .countByUserIdAndIsCompletedTrueAndDueDateBetween(userId, startOfWeekLdt, endOfWeekLdt);

        List<Object[]> categoryCounts = taskRepository.countCompletedTasksByCategory(userId);
        Map<String, Long> categoryDistribution = new LinkedHashMap<>();
        for (Object[] result : categoryCounts) {
            String categoryName = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            categoryDistribution.put(categoryName != null ? categoryName : "Kategorisiz", count);
        }

        Long totalPomodoro = pomodoroRepository.sumDurationByUserId(userId);
        long pomodoroMinutes = (totalPomodoro != null) ? totalPomodoro : 0L;

        long pendingTasks = taskRepository.countByUserIdAndIsCompletedFalse(userId);

        Instant weekStart = startOfWeekLdt.atZone(zone).toInstant();
        Instant weekEnd   = endOfWeekLdt.atZone(zone).toInstant();
        List<Task> weeklyTasks = taskRepository
                .findAllByUserIdAndIsCompletedTrueAndUpdatedAtBetween(userId, weekStart, weekEnd);

        Map<String, Long> weeklyProductivity = new LinkedHashMap<>();
        for (String day : TR_DAYS) weeklyProductivity.put(day, 0L);
        for (Task t : weeklyTasks) {
            if (t.getUpdatedAt() == null) continue;
            DayOfWeek dow = t.getUpdatedAt().atZone(zone).getDayOfWeek();
            String key = TR_DAYS[dow.getValue() - 1];
            weeklyProductivity.merge(key, 1L, Long::sum);
        }

        long weeklyTotal = weeklyCompleted + taskRepository.countByUserIdAndIsCompletedFalse(userId);
        double goalCompletionRate = weeklyTotal > 0
                ? Math.round((weeklyCompleted * 100.0 / weeklyTotal) * 10.0) / 10.0
                : 0.0;

        int dailyStreak = computeDailyStreak(userId, zone);

        return new StatisticsResponseDTO(
                totalCompleted,
                weeklyCompleted,
                categoryDistribution,
                pomodoroMinutes,
                pendingTasks,
                dailyStreak,
                goalCompletionRate,
                weeklyProductivity
        );
    }

    private int computeDailyStreak(Long userId, ZoneId zone) {
        List<Task> completed = taskRepository.findAllByUserIdAndIsCompletedTrueOrderByUpdatedAtDesc(userId);
        if (completed.isEmpty()) return 0;

        Set<LocalDate> completedDays = completed.stream()
                .filter(t -> t.getUpdatedAt() != null)
                .map(t -> t.getUpdatedAt().atZone(zone).toLocalDate())
                .collect(Collectors.toSet());

        LocalDate cursor = LocalDate.now();
        int streak = 0;
        while (completedDays.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private void enforceCurrentUser(Long userId) {
        Long current = SecurityUtil.currentUserIdOrNull();
        if (current != null && !current.equals(userId)) {
            throw new NotFoundException("User not found or access denied");
        }
    }

    @Override
    public HeatmapResponseDTO getHeatmap(Long userId, int days) {
        enforceCurrentUser(userId);
        ZoneId zone = ZoneId.systemDefault();

        LocalDate today = LocalDate.now(zone);
        LocalDate from  = today.minusDays(days - 1);

        Instant start = from.atStartOfDay(zone).toInstant();
        Instant end   = today.plusDays(1).atStartOfDay(zone).toInstant();

        List<Task> completed = taskRepository
                .findAllByUserIdAndIsCompletedTrueAndUpdatedAtBetween(userId, start, end);

        Map<String, Integer> dailyCounts = new LinkedHashMap<>();
        for (int i = days - 1; i >= 0; i--) {
            dailyCounts.put(today.minusDays(i).toString(), 0);
        }

        for (Task t : completed) {
            if (t.getUpdatedAt() == null) continue;
            String dateKey = t.getUpdatedAt().atZone(zone).toLocalDate().toString();
            dailyCounts.computeIfPresent(dateKey, (k, v) -> v + 1);
        }

        return new HeatmapResponseDTO(dailyCounts);
    }
}
