package com.sama.RapidGaz.mappers;

import com.sama.RapidGaz.model.GasSellerLocation;
import com.sama.RapidGaz.responses.GasSellerLocationResponse;

public class GasSellerLocationMapper {

    public static GasSellerLocationResponse toResponse(GasSellerLocation location) {
        GasSellerLocationResponse response = new GasSellerLocationResponse();
        response.setId(location.getId());
        response.setLatitude(location.getLatitude());
        response.setLongitude(location.getLongitude());
        response.setSeller(GasSellerMapper.toSummary(location.getSeller()));
        return response;
    }
}
