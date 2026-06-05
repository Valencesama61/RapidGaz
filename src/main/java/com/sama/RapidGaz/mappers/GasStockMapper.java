package com.sama.RapidGaz.mappers;

import com.sama.RapidGaz.model.GasStock;
import com.sama.RapidGaz.responses.GasStockResponse;

public class GasStockMapper {

    public static GasStockResponse toResponse(GasStock stock) {
        GasStockResponse response = new GasStockResponse();
        response.setId(stock.getId());
        response.setQuantity(stock.getQuantity());
        response.setProduct(GasProductMapper.toResponse(stock.getProduct()));
        return response;
    }
}
