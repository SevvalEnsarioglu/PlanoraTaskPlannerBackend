package com.sevval.PlanoraTaskPlannerBackend.service.impl;

import com.sevval.PlanoraTaskPlannerBackend.exception.NotFoundException;
import com.sevval.PlanoraTaskPlannerBackend.mapper.TagMapper;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.request.TagRequestDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.dto.response.TagResponseDTO;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.Tag;
import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import com.sevval.PlanoraTaskPlannerBackend.repository.TagRepository;
import com.sevval.PlanoraTaskPlannerBackend.repository.UserRepository;
import com.sevval.PlanoraTaskPlannerBackend.security.SecurityUtil;
import com.sevval.PlanoraTaskPlannerBackend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDTO> list(Long userId) {
        enforceCurrentUser(userId);
        return tagRepository.findAllByUserId(userId).stream().map(tagMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TagResponseDTO get(Long userId, Long tagId) {
        enforceCurrentUser(userId);
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        return tagMapper.toResponseDTO(tag);
    }

    @Override
    public TagResponseDTO create(Long userId, TagRequestDTO request) {
        enforceCurrentUser(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Tag tag = tagMapper.toEntity(request);
        tag.setUser(user);
        Tag saved = tagRepository.save(tag);
        return tagMapper.toResponseDTO(saved);
    }

    @Override
    public TagResponseDTO update(Long userId, Long tagId, TagRequestDTO request) {
        enforceCurrentUser(userId);
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        tag.setName(request.name());
        tag.setColorCode(request.colorCode());
        return tagMapper.toResponseDTO(tag);
    }

    @Override
    public void delete(Long userId, Long tagId) {
        enforceCurrentUser(userId);
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }

    private static void enforceCurrentUser(Long userId) {
        Long current = SecurityUtil.currentUserIdOrNull();
        if (current != null && !current.equals(userId)) {
            throw new NotFoundException("Tag not found");
        }
    }
}


