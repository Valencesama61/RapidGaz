package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AdminSellerSummary {
    private Long id;
    private String displayName;
    private String email;
    private String phone;
    private Boolean isOpen;
    private Boolean isActive;
    private Date createdAt;
}
