package com.sevval.PlanoraTaskPlannerBackend.model.dto.response;

import java.util.Map;

public record StatisticsResponseDTO(
        long totalCompletedTasks,
        long weeklyCompletedTasks,
        Map<String, Long> categoryDistribution,
        long totalPomodoroMinutes,
        long pendingTasks,
        int dailyStreak,
        double goalCompletionRate,
        Map<String, Long> weeklyProductivity
) {
}
