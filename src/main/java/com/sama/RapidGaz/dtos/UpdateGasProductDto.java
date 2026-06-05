package com.sama.RapidGaz.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateGasProductDto {

    private String brand;
    private String size;

    @Positive(message = "Le prix doit être une valeur positive")
    private Double price;
}
