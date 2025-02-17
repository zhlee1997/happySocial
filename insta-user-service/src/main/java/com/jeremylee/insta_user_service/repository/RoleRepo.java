package com.jeremylee.insta_user_service.repository;

import com.jeremylee.insta_user_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}
