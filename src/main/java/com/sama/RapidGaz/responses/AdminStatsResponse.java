package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AdminStatsResponse {
    private long totalSellers;
    private long activeSellers;
    private long suspendedSellers;
    private long openSellers;
    private long totalProducts;
    private long totalAdmins;
}
