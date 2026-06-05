package com.sama.RapidGaz.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterGasSellerDto {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(
            min = 3,
            max = 50,
            message = "Le nom doit contenir entre 3 et 50 caractères"
    )
    private String displayname;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Size(
            min = 8,
            max = 12,
            message = "Le numéro de téléphone doit avoir entre 8 et 10 chiffres"
    )
    private String phone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(
            min = 6,
            message = "Le mot de passe doit contenir au moins 6 caractères"
    )
    private String password;
}