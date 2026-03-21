package com.sevval.PlanoraTaskPlannerBackend.controller;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.StatisticsResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{userId}/statistics")
@RequiredArgsConstructor
@Validated
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public StatisticsResponseDTO getUserStatistics(@PathVariable Long userId) {
        return statisticsService.getUserStatistics(userId);
    }
}
