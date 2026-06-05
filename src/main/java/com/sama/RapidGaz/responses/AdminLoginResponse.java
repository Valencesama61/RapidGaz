package com.sama.RapidGaz.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AdminLoginResponse {
    private String accessToken;
    private String tokenType;
    private String role;
    private String name;

    @JsonIgnore
    private String refreshToken;
}
