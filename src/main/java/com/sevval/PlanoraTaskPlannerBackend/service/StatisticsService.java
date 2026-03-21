package com.sevval.PlanoraTaskPlannerBackend.service;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.StatisticsResponseDTO;

public interface StatisticsService {
    StatisticsResponseDTO getUserStatistics(Long userId);
}
