package com.sama.RapidGaz.controllers.admin;

import com.sama.RapidGaz.dtos.CreateAdminRequest;
import com.sama.RapidGaz.model.CatalogBrand;
import com.sama.RapidGaz.model.CatalogSize;
import com.sama.RapidGaz.responses.AdminResponse;
import com.sama.RapidGaz.responses.AdminSellerDetail;
import com.sama.RapidGaz.responses.AdminSellerSummary;
import com.sama.RapidGaz.responses.AdminStatsResponse;
import com.sama.RapidGaz.responses.PagedResponse;
import com.sama.RapidGaz.services.AdminService;
import com.sama.RapidGaz.services.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService   adminService;
    private final CatalogService catalogService;

    // ── Admins ───────────────────────────────────────────────────────────────

    @PostMapping("/users")
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(request));
    }

    @GetMapping("/users")
    public ResponseEntity<PagedResponse<AdminResponse>> listAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.listAdmins(page, Math.min(size, 100)));
    }

    // ── Vendeurs ─────────────────────────────────────────────────────────────

    @GetMapping("/sellers")
    public ResponseEntity<PagedResponse<AdminSellerSummary>> listSellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.listSellers(page, Math.min(size, 100)));
    }

    @GetMapping("/sellers/{id}")
    public ResponseEntity<AdminSellerDetail> getSellerDetail(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getSellerDetail(id));
    }

    @PutMapping("/sellers/{id}/suspend")
    public ResponseEntity<Void> suspendSeller(@PathVariable Long id) {
        adminService.suspendSeller(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sellers/{id}/reactivate")
    public ResponseEntity<Void> reactivateSeller(@PathVariable Long id) {
        adminService.reactivateSeller(id);
        return ResponseEntity.noContent().build();
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    // ── Catalogue marques ─────────────────────────────────────────────────────

    @GetMapping("/catalog/brands")
    public ResponseEntity<List<CatalogBrand>> getBrands() {
        return ResponseEntity.ok(catalogService.getBrands());
    }

    @PostMapping("/catalog/brands")
    public ResponseEntity<CatalogBrand> addBrand(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(catalogService.addBrand(body.get("name")));
    }

    @DeleteMapping("/catalog/brands/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        catalogService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    // ── Catalogue tailles ─────────────────────────────────────────────────────

    @GetMapping("/catalog/sizes")
    public ResponseEntity<List<CatalogSize>> getSizes() {
        return ResponseEntity.ok(catalogService.getSizes());
    }

    @PostMapping("/catalog/sizes")
    public ResponseEntity<CatalogSize> addSize(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(catalogService.addSize(body.get("name")));
    }

    @DeleteMapping("/catalog/sizes/{id}")
    public ResponseEntity<Void> deleteSize(@PathVariable Long id) {
        catalogService.deleteSize(id);
        return ResponseEntity.noContent().build();
    }
}
