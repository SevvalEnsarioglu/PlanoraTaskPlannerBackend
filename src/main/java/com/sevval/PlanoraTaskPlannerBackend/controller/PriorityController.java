package com.sevval.PlanoraTaskPlannerBackend.controller;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.PriorityRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.PriorityResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.service.PriorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/priorities")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    public List<PriorityResponseDTO> list(@PathVariable Long userId) {
        return priorityService.list(userId);
    }

    @GetMapping("/{priorityId}")
    public PriorityResponseDTO get(@PathVariable Long userId, @PathVariable Long priorityId) {
        return priorityService.get(userId, priorityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PriorityResponseDTO create(@PathVariable Long userId, @RequestBody @Valid PriorityRequestDTO request) {
        return priorityService.create(userId, request);
    }

    @PutMapping("/{priorityId}")
    public PriorityResponseDTO update(
            @PathVariable Long userId,
            @PathVariable Long priorityId,
            @RequestBody @Valid PriorityRequestDTO request
    ) {
        return priorityService.update(userId, priorityId, request);
    }

    @DeleteMapping("/{priorityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long priorityId) {
        priorityService.delete(userId, priorityId);
    }
}


