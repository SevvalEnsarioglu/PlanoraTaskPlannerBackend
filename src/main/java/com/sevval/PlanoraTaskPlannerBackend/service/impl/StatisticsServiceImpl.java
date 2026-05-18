package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
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

    // Türkçe gün kısaltmaları: Pazartesi(1) → Paz(7)
    private static final String[] TR_DAYS = {"Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz"};

    @Override
    public StatisticsResponseDTO getUserStatistics(Long userId) {
        enforceCurrentUser(userId);

        ZoneId zone = ZoneId.systemDefault();

        // ── 1. Toplam tamamlanan ──────────────────────────────────────────────
        long totalCompleted = taskRepository.countByUserIdAndIsCompletedTrue(userId);

        // ── 2. Haftalık tamamlanan (dueDate bazlı, mevcut mantık) ────────────
        LocalDateTime startOfWeekLdt = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        LocalDateTime endOfWeekLdt = startOfWeekLdt.plusDays(7).minusNanos(1);
        long weeklyCompleted = taskRepository
                .countByUserIdAndIsCompletedTrueAndDueDateBetween(userId, startOfWeekLdt, endOfWeekLdt);

        // ── 3. Kategori dağılımı ──────────────────────────────────────────────
        List<Object[]> categoryCounts = taskRepository.countCompletedTasksByCategory(userId);
        Map<String, Long> categoryDistribution = new LinkedHashMap<>();
        for (Object[] result : categoryCounts) {
            String categoryName = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            categoryDistribution.put(categoryName != null ? categoryName : "Kategorisiz", count);
        }

        // ── 4. Toplam pomodoro süresi ─────────────────────────────────────────
        Long totalPomodoro = pomodoroRepository.sumDurationByUserId(userId);
        long pomodoroMinutes = (totalPomodoro != null) ? totalPomodoro : 0L;

        // ── 5. Bekleyen görev sayısı ──────────────────────────────────────────
        long pendingTasks = taskRepository.countByUserIdAndIsCompletedFalse(userId);

        // ── 6. Haftalık üretkenlik (Pzt→Paz kırılımı, updatedAt bazlı) ───────
        Instant weekStart = startOfWeekLdt.atZone(zone).toInstant();
        Instant weekEnd   = endOfWeekLdt.atZone(zone).toInstant();
        List<Task> weeklyTasks = taskRepository
                .findAllByUserIdAndIsCompletedTrueAndUpdatedAtBetween(userId, weekStart, weekEnd);

        Map<String, Long> weeklyProductivity = new LinkedHashMap<>();
        for (String day : TR_DAYS) weeklyProductivity.put(day, 0L);
        for (Task t : weeklyTasks) {
            if (t.getUpdatedAt() == null) continue;
            DayOfWeek dow = t.getUpdatedAt().atZone(zone).getDayOfWeek();
            String key = TR_DAYS[dow.getValue() - 1]; // MONDAY=1 → idx 0
            weeklyProductivity.merge(key, 1L, Long::sum);
        }

        // ── 7. Hedef tamamlanma oranı (bu haftaki tamamlanan / toplam) ────────
        long weeklyTotal = weeklyCompleted + taskRepository.countByUserIdAndIsCompletedFalse(userId);
        double goalCompletionRate = weeklyTotal > 0
                ? Math.round((weeklyCompleted * 100.0 / weeklyTotal) * 10.0) / 10.0
                : 0.0;

        // ── 8. Günlük seri (ard arda tamamlanan gün sayısı) ──────────────────
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

    /**
     * Son gün itibariyle ard arda kaç gün boyunca en az 1 görev tamamlanmış.
     * updatedAt alanı tamamlanma tarihi olarak kullanılır.
     */
    private int computeDailyStreak(Long userId, ZoneId zone) {
        List<Task> completed = taskRepository.findAllByUserIdAndIsCompletedTrueOrderByUpdatedAtDesc(userId);
        if (completed.isEmpty()) return 0;

        // Benzersiz tamamlanma günleri kümesi (LocalDate)
        Set<LocalDate> completedDays = completed.stream()
                .filter(t -> t.getUpdatedAt() != null)
                .map(t -> t.getUpdatedAt().atZone(zone).toLocalDate())
                .collect(Collectors.toSet());

        // Bugünden geriye say
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
}

