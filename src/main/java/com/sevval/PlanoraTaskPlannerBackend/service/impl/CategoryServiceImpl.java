package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.mapper.CategoryMapper;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.CategoryRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.CategoryResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Category;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import com.sevval.PlanoraTaskPlannerBackend.repository.CategoryRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.UserRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil;
import com.sevval.PlanoraTaskPlannerBackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> list(Long userId) {
        enforceCurrentUser(userId);
        return categoryRepository.findAllByUserId(userId).stream().map(categoryMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO get(Long userId, Long categoryId) {
        enforceCurrentUser(userId);
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO create(Long userId, CategoryRequestDTO request) {
        enforceCurrentUser(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Category category = categoryMapper.toEntity(request);
        category.setUser(user);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(saved);
    }

    @Override
    public CategoryResponseDTO update(Long userId, Long categoryId, CategoryRequestDTO request) {
        enforceCurrentUser(userId);
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(request.name());
        category.setColorCode(request.colorCode());
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public void delete(Long userId, Long categoryId) {
        enforceCurrentUser(userId);
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    private static void enforceCurrentUser(Long userId) {
        Long current = SecurityUtil.currentUserIdOrNull();
        if (current != null && !current.equals(userId)) {
            throw new NotFoundException("Category not found");
        }
    }
}


