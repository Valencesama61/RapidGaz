package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductInResult {
    private String brand;
    private String size;
    private Double price;
    private Integer stockQuantity;
}
