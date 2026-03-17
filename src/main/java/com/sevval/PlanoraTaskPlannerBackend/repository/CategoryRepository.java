package com.sevval.PlanoraTaskPlannerBackend.repository;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
