package com.sama.RapidGaz.repository;

import com.sama.RapidGaz.model.CatalogBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogBrandRepository extends JpaRepository<CatalogBrand, Long> {
    Optional<CatalogBrand> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
