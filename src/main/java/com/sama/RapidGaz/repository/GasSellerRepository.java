package com.sama.RapidGaz.repository;

import com.sama.RapidGaz.model.GasSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GasSellerRepository extends JpaRepository<GasSeller, Long> {
    Optional<GasSeller> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByIsActive(Boolean isActive);
    long countByIsOpen(Boolean isOpen);

    @Query(value = """
        SELECT tmp.displayname, tmp.phone, tmp.latitude, tmp.longitude,
               tmp.brand, tmp.size, tmp.price, tmp.quantity, tmp.distance
        FROM (
            SELECT
                gs.displayname,
                gs.phone,
                gl.latitude,
                gl.longitude,
                gp.brand,
                gp.size,
                gp.price,
                gst.quantity,
                (6371 * acos(
                    cos(radians(:lat)) * cos(radians(gl.latitude))
                    * cos(radians(gl.longitude) - radians(:lng))
                    + sin(radians(:lat)) * sin(radians(gl.latitude))
                )) AS distance
            FROM gas_seller gs
            JOIN gas_seller_location gl ON gl.seller_id = gs.id
            JOIN gas_product gp         ON gp.seller_id = gs.id
            JOIN gas_stock gst          ON gst.product_id = gp.id
            WHERE gs.is_active = true
              AND gs.is_open   = true
              AND gp.brand     = :brand
              AND gp.size      = :size
              AND gst.quantity > 0
        ) tmp
        WHERE tmp.distance <= :radius
        ORDER BY tmp.distance ASC
        LIMIT 3
    """, nativeQuery = true)
    List<Object[]> findTop3NearbyWithProduct(
            @Param("lat")    Double latitude,
            @Param("lng")    Double longitude,
            @Param("radius") Double radius,
            @Param("brand")  String brand,
            @Param("size")   String size
    );
}
