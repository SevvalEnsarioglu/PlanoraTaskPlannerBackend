package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.HeatmapResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.StatisticsResponseDTO;

public interface StatisticsService {
    StatisticsResponseDTO getUserStatistics(Long userId);
    HeatmapResponseDTO getHeatmap(Long userId, int days);
}
