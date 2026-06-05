package com.sama.RapidGaz.controllers.v1;

import com.sama.RapidGaz.dtos.GasSellerChangePasswordDto;
import com.sama.RapidGaz.dtos.UpdateGasSellerDto;
import com.sama.RapidGaz.mappers.GasSellerMapper;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.responses.GasSellerSummary;
import com.sama.RapidGaz.services.GasSellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seller")
public class GasSellerController {

    private final GasSellerService gasSellerService;

    @GetMapping("/me")
    public ResponseEntity<GasSellerSummary> me(@AuthenticationPrincipal GasSeller currentGasSeller) {
        return ResponseEntity.ok(GasSellerMapper.toSummary(currentGasSeller));
    }

    @PutMapping("/me")
    public ResponseEntity<GasSellerSummary> update(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @Valid @RequestBody UpdateGasSellerDto input) {
        GasSeller gasSeller = gasSellerService.update(currentGasSeller, input);
        return ResponseEntity.ok(GasSellerMapper.toSummary(gasSeller));
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal GasSeller currentGasSeller,
            @Valid @RequestBody GasSellerChangePasswordDto input
    ) {
        gasSellerService.changePass(currentGasSeller.getEmail(), input);

        Map<String, String> res = new HashMap<>();
        res.put("status", "success");
        return ResponseEntity.ok(res);
    }
}
