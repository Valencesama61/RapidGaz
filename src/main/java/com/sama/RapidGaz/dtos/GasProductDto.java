package com.sama.RapidGaz.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GasProductDto {

    @NotBlank(message = "La marque du gaz est obligatoire")
    private String brand;

    @NotBlank(message = "La taille du gaz est obligatoire")
    private String size;

    @NotNull(message = "Le prix du produit ne peut être nul")
    @Positive(message = "Le prix doit être une valeur positive")
    private Double price;
}
