package com.sama.RapidGaz.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GasStockDto {

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 0, message = "La quantité doit être au minimum 0")
    private Integer quantity;




}
