package com.sama.RapidGaz.controllers.v1;

import com.sama.RapidGaz.dtos.GasSellerLocationDto;
import com.sama.RapidGaz.mappers.GasSellerLocationMapper;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.model.GasSellerLocation;
import com.sama.RapidGaz.responses.GasSellerLocationResponse;
import com.sama.RapidGaz.services.GasSellerLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seller/me")
public class GasSellerLocationController {

    private final GasSellerLocationService gasSellerLocationService;

    @PostMapping("/location")
    public ResponseEntity<GasSellerLocationResponse> addLocation(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @Valid @RequestBody GasSellerLocationDto input
    ) {
        GasSellerLocation location = gasSellerLocationService.addLocation(currentGasSeller.getId(), input);
        return ResponseEntity.status(HttpStatus.CREATED).body(GasSellerLocationMapper.toResponse(location));
    }

    @PutMapping("/location")
    public ResponseEntity<GasSellerLocationResponse> updateLocation(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @Valid @RequestBody GasSellerLocationDto input
    ) {
        GasSellerLocation location = gasSellerLocationService.updateLocation(currentGasSeller.getId(), input);
        return ResponseEntity.ok(GasSellerLocationMapper.toResponse(location));
    }

    @GetMapping("/location")
    public ResponseEntity<GasSellerLocationResponse> myLocation(
            @AuthenticationPrincipal GasSeller currentGasSeller
    ) {
        GasSellerLocation location = gasSellerLocationService.myLocation(currentGasSeller.getId());
        return ResponseEntity.ok(GasSellerLocationMapper.toResponse(location));
    }
}
