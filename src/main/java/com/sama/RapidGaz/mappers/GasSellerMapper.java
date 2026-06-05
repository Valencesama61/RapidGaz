package com.sama.RapidGaz.mappers;

import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.responses.GasSellerSummary;

public class GasSellerMapper {

    public static GasSellerSummary toSummary(GasSeller seller) {
        GasSellerSummary summary = new GasSellerSummary();
        summary.setId(seller.getId());
        summary.setDisplayName(seller.getDisplayname());
        summary.setPhone(seller.getPhone());
        summary.setIsOpen(seller.getIsOpen());
        return summary;
    }
}
