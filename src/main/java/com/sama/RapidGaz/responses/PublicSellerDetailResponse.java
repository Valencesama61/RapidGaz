package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PublicSellerDetailResponse {
    private String displayName;
    private String phone;
    private Boolean isOpen;
    private List<GasProductResponse> products;
}
