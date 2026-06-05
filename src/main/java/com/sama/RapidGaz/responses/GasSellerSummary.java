package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GasSellerSummary {
    private Long id;
    private String displayName;
    private String phone;
    private Boolean isOpen;
}
