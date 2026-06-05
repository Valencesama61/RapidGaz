package com.sama.RapidGaz.repository;

import com.sama.RapidGaz.enums.AdminRole;
import com.sama.RapidGaz.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByRole(AdminRole role);
    boolean existsByEmail(String email);
    boolean existsByRole(AdminRole role);
}
