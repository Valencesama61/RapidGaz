package com.sama.RapidGaz.repository;

import com.sama.RapidGaz.model.GasStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GasStockRepository extends JpaRepository<GasStock, Long> {
    Optional<GasStock> findBySellerIdAndProductId(Long sellerId, Long productId);
    void deleteByProductId(Long productId);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM GasStock s WHERE s.seller.id = :sellerId")
    int sumQuantityBySellerId(@Param("sellerId") Long sellerId);
}
