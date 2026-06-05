package com.sama.RapidGaz.dtos;

import jakarta.validation.constraints.Size;
import  lombok.Data;
import lombok.experimental.Accessors;

@Data
    @Accessors(chain = true)

public class UpdateGasSellerDto {

    @Size(
            min = 3,
            max = 50,
            message = "Le nom doit contenir entre 3 et 50 caractères"
    )
    private String displayName;

    @Size(
            min = 8,
            max = 12,
            message = "Le numéro de téléphone doit avoir entre 8 et 10 chiffres"
    )
    private String phone;

    private Boolean isOpen;
}
