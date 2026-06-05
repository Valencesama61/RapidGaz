package com.sama.RapidGaz.controllers.v1;

import com.sama.RapidGaz.responses.PublicSellerDetailResponse;
import com.sama.RapidGaz.responses.SearchResultResponse;
import com.sama.RapidGaz.services.CatalogService;
import com.sama.RapidGaz.services.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {

    private final PublicService   publicService;
    private final CatalogService  catalogService;

    @GetMapping("/sellers")
    public ResponseEntity<List<SearchResultResponse>> search(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius,
            @RequestParam String brand,
            @RequestParam String size
    ) {
        return ResponseEntity.ok(publicService.searchSellers(latitude, longitude, radius, brand, size));
    }

    @GetMapping("/sellers/{id}")
    public ResponseEntity<PublicSellerDetailResponse> getSellerDetail(@PathVariable Long id) {
        return ResponseEntity.ok(publicService.getSellerDetail(id));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        List<String> brands = catalogService.getBrands().stream()
                .map(b -> b.getName())
                .toList();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/sizes")
    public ResponseEntity<List<String>> getSizes() {
        List<String> sizes = catalogService.getSizes().stream()
                .map(s -> s.getName())
                .toList();
        return ResponseEntity.ok(sizes);
    }
}
