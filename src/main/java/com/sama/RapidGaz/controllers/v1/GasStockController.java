package com.sama.RapidGaz.controllers.v1;

import com.sama.RapidGaz.dtos.GasStockDto;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.responses.GasStockResponse;
import com.sama.RapidGaz.services.GasStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller/me/products/{id}")
@RequiredArgsConstructor
public class GasStockController {

    private final GasStockService gasStockService;

    @GetMapping("/stock")
    public ResponseEntity<GasStockResponse> getGasStock(
            @PathVariable Long id,
            @AuthenticationPrincipal GasSeller currentGasSeller
    ) {
        return ResponseEntity.ok(gasStockService.getStock(id, currentGasSeller.getId()));
    }

    @PutMapping("/stock")
    public ResponseEntity<GasStockResponse> updateGasStock(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @PathVariable Long id,
            @Valid @RequestBody GasStockDto input
    ) {
        return ResponseEntity.ok(gasStockService.update(id, currentGasSeller.getId(), input));
    }

    @PatchMapping("/increment")
    public ResponseEntity<GasStockResponse> incrementGasStock(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(gasStockService.increment(id, currentGasSeller.getId()));
    }

    @PatchMapping("/decrement")
    public ResponseEntity<GasStockResponse> decrementGasStock(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(gasStockService.decrement(id, currentGasSeller.getId()));
    }

    @PatchMapping("/reset")
    public ResponseEntity<GasStockResponse> resetGasStock(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(gasStockService.reset(id, currentGasSeller.getId()));
    }
}
