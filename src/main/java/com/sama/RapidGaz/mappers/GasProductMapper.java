package com.sama.RapidGaz.mappers;

import com.sama.RapidGaz.model.GasProduct;
import com.sama.RapidGaz.responses.GasProductResponse;

public class GasProductMapper {

    public static GasProductResponse toResponse(GasProduct product) {
        return toResponse(product, null);
    }

    public static GasProductResponse toResponse(GasProduct product, Integer stockQuantity) {
        GasProductResponse response = new GasProductResponse();
        response.setId(product.getId());
        response.setBrand(product.getBrand());
        response.setSize(product.getSize());
        response.setPrice(product.getPrice());
        response.setStockQuantity(stockQuantity);
        return response;
    }
}
