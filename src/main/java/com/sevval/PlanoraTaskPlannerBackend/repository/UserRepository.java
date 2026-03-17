package com.sevval.PlanoraTaskPlannerBackend.repository;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
