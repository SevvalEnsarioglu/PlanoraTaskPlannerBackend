package com.sevval.PlanoraTaskPlannerBackend.model.dto.response;

import java.util.Map;

public record HeatmapResponseDTO(
        Map<String, Integer> dailyCounts   // {"2025-05-01": 3, "2025-05-02": 0, ...}
) {
}
