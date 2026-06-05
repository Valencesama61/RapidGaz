package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AdminSellerDetail {
    private Long id;
    private String displayName;
    private String email;
    private String phone;
    private Boolean isOpen;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    // Localisation
    private Double longitude;
    private Double latitude;

    // Métriques
    private int productCount;
    private int totalStock;
}
