package com.sama.RapidGaz.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterResponse {

    private Long id;

    private String displayname;

    private String email;

    private String phone;

    private Boolean isOpen;
}