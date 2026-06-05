package com.sama.RapidGaz.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class GasSellerChangePasswordDto {

    @NotBlank(message = "L'ancien mot de passe est obligatoire")
    private String oldPassword;

    @Size(
            min = 6,
            message = "Le mot de passe doit contenir au moins 6 caractères"
    )
    private String newPassword;
}
