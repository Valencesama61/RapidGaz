package com.sama.RapidGaz.repository;

import com.sama.RapidGaz.model.CatalogSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogSizeRepository extends JpaRepository<CatalogSize, Long> {
    Optional<CatalogSize> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
