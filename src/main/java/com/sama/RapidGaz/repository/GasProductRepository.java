package com.sama.RapidGaz.repository;

import com.sama.RapidGaz.model.GasProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GasProductRepository extends JpaRepository<GasProduct, Long> {
    List<GasProduct> findAllBySellerId(Long sellerId);
    Optional<GasProduct> findByBrandAndSizeAndSellerId(String brand, String size, Long sellerId);
}
