package com.sama.RapidGaz.responses;

import com.sama.RapidGaz.enums.AdminRole;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AdminResponse {
    private Long id;
    private String name;
    private String email;
    private AdminRole role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
