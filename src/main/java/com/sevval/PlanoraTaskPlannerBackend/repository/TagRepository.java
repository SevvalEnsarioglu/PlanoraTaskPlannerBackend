package com.sevval.PlanoraTaskPlannerBackend.repository;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
