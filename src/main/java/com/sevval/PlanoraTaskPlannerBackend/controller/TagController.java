package com.sevval.PlanoraTaskPlannerBackend.controller;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TagRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TagResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<TagResponseDTO> list(@PathVariable Long userId) {
        return tagService.list(userId);
    }

    @GetMapping("/{tagId}")
    public TagResponseDTO get(@PathVariable Long userId, @PathVariable Long tagId) {
        return tagService.get(userId, tagId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponseDTO create(@PathVariable Long userId, @RequestBody @Valid TagRequestDTO request) {
        return tagService.create(userId, request);
    }

    @PutMapping("/{tagId}")
    public TagResponseDTO update(
            @PathVariable Long userId,
            @PathVariable Long tagId,
            @RequestBody @Valid TagRequestDTO request
    ) {
        return tagService.update(userId, tagId, request);
    }

    @DeleteMapping("/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long tagId) {
        tagService.delete(userId, tagId);
    }
}


