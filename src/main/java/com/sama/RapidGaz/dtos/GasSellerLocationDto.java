package com.sama.RapidGaz.dtos;
import lombok.Data;
import jakarta.validation.constraints.NotNull;


@Data
public class GasSellerLocationDto {

    @NotNull(message = "La donnée latitude ne peut être nulle")
    private Double latitude;

    @NotNull(message = "La donnée Longitude ne peut être nulle")
    private Double longitude;
}
