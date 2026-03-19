package com.sevval.PlanoraTaskPlannerBackend.controller;

import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.CategoryRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.CategoryResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> list(@PathVariable Long userId) {
        return categoryService.list(userId);
    }

    @GetMapping("/{categoryId}")
    public CategoryResponseDTO get(@PathVariable Long userId, @PathVariable Long categoryId) {
        return categoryService.get(userId, categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDTO create(@PathVariable Long userId, @RequestBody @Valid CategoryRequestDTO request) {
        return categoryService.create(userId, request);
    }

    @PutMapping("/{categoryId}")
    public CategoryResponseDTO update(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryRequestDTO request
    ) {
        return categoryService.update(userId, categoryId, request);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long categoryId) {
        categoryService.delete(userId, categoryId);
    }
}


