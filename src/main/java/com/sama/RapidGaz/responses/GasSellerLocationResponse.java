package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GasSellerLocationResponse {
    private Long id;
    private Double longitude;
    private Double latitude;
    private GasSellerSummary seller;
}
