package com.sama.RapidGaz.responses;

import lombok.Data;

@Data
public class GasStockResponse {
    private Long id;
    private Integer quantity;
    private GasProductResponse product;
}
