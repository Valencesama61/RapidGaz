package com.sama.RapidGaz.repository;

import com.sama.RapidGaz.model.GasSellerLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GasSellerLocationRepository extends JpaRepository<GasSellerLocation, Long> {
    Optional<GasSellerLocation> findBySellerId(Long id);
}
