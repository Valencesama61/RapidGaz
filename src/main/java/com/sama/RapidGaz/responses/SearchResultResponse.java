package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchResultResponse {
    private String displayName;
    private String phone;
    private Double distance;
    private String googleMapsUrl;
    private ProductInResult product;
}
