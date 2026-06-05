package com.sama.RapidGaz.controllers.v1;

import com.sama.RapidGaz.dtos.GasProductDto;
import com.sama.RapidGaz.dtos.UpdateGasProductDto;
import com.sama.RapidGaz.enums.GasBrand;
import com.sama.RapidGaz.enums.GasSize;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.responses.GasProductResponse;
import com.sama.RapidGaz.services.GasProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seller/me")
public class GasProductController {

    private final GasProductService gasProductService;

    @GetMapping("/products/options")
    public ResponseEntity<Map<String, Object>> getProductOptions() {
        return ResponseEntity.ok(Map.of(
                "brands", GasBrand.values(),
                "sizes", GasSize.values()
        ));
    }

    @PostMapping("/products")
    public ResponseEntity<GasProductResponse> createGasProduct(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @Valid @RequestBody GasProductDto input
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gasProductService.create(currentGasSeller.getId(), input));
    }

    @GetMapping("/products")
    public ResponseEntity<List<GasProductResponse>> getGasProducts(
            @AuthenticationPrincipal GasSeller currentGasSeller
    ) {
        return ResponseEntity.ok(gasProductService.getAll(currentGasSeller.getId()));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<GasProductResponse> getGasProductById(
            @PathVariable Long id,
            @AuthenticationPrincipal GasSeller currentGasSeller
    ) {
        return ResponseEntity.ok(gasProductService.getOne(id, currentGasSeller.getId()));
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<GasProductResponse> updateGasProduct(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @PathVariable Long id,
            @Valid @RequestBody UpdateGasProductDto input
    ) {
        return ResponseEntity.ok(gasProductService.update(currentGasSeller.getId(), id, input));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteGasProductById(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @PathVariable Long id
    ) {
        gasProductService.remove(id, currentGasSeller.getId());
        return ResponseEntity.noContent().build();
    }
}
