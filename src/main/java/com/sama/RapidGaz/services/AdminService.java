package com.sama.RapidGaz.services;

import com.sama.RapidGaz.dtos.CreateAdminRequest;
import com.sama.RapidGaz.enums.AdminRole;
import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.exceptions.RessourceExistException;
import com.sama.RapidGaz.model.Admin;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.repository.AdminRepository;
import com.sama.RapidGaz.repository.GasProductRepository;
import com.sama.RapidGaz.repository.GasSellerLocationRepository;
import com.sama.RapidGaz.repository.GasSellerRepository;
import com.sama.RapidGaz.repository.GasStockRepository;
import com.sama.RapidGaz.responses.AdminResponse;
import com.sama.RapidGaz.responses.AdminSellerDetail;
import com.sama.RapidGaz.responses.AdminSellerSummary;
import com.sama.RapidGaz.responses.AdminStatsResponse;
import com.sama.RapidGaz.responses.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final GasSellerRepository gasSellerRepository;
    private final GasProductRepository gasProductRepository;
    private final GasSellerLocationRepository gasSellerLocationRepository;
    private final GasStockRepository gasStockRepository;
    private final PasswordEncoder passwordEncoder;

    // ─── Gestion des admins (SUPER_ADMIN) ───────────────────────────────────

    public AdminResponse createAdmin(CreateAdminRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RessourceExistException("Cet email est déjà utilisé par un autre admin.");
        }

        Admin admin = new Admin()
                .setName(request.getName())
                .setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setRole(AdminRole.ADMIN)
                .setIsActive(true);

        Admin saved = adminRepository.save(admin);
        return toAdminResponse(saved);
    }

    public PagedResponse<AdminResponse> listAdmins(int page, int size) {
        Page<Admin> adminPage = adminRepository.findAll(PageRequest.of(page, size));
        List<AdminResponse> content = adminPage.getContent().stream()
                .map(this::toAdminResponse)
                .toList();

        return new PagedResponse<AdminResponse>()
                .setContent(content)
                .setPage(adminPage.getNumber())
                .setSize(adminPage.getSize())
                .setTotalElements(adminPage.getTotalElements())
                .setTotalPages(adminPage.getTotalPages());
    }

    // ─── Gestion des vendeurs (ADMIN + SUPER_ADMIN) ──────────────────────────

    public PagedResponse<AdminSellerSummary> listSellers(int page, int size) {
        Page<GasSeller> sellerPage = gasSellerRepository.findAll(PageRequest.of(page, size));
        List<AdminSellerSummary> content = sellerPage.getContent().stream()
                .map(this::toSellerSummary)
                .toList();

        return new PagedResponse<AdminSellerSummary>()
                .setContent(content)
                .setPage(sellerPage.getNumber())
                .setSize(sellerPage.getSize())
                .setTotalElements(sellerPage.getTotalElements())
                .setTotalPages(sellerPage.getTotalPages());
    }

    public AdminSellerDetail getSellerDetail(Long id) {
        GasSeller seller = gasSellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendeur introuvable"));

        AdminSellerDetail detail = new AdminSellerDetail()
                .setId(seller.getId())
                .setDisplayName(seller.getDisplayname())
                .setEmail(seller.getEmail())
                .setPhone(seller.getPhone())
                .setIsOpen(seller.getIsOpen())
                .setIsActive(seller.getIsActive())
                .setCreatedAt(seller.getCreatedAt())
                .setUpdatedAt(seller.getUpdatedAt());

        gasSellerLocationRepository.findBySellerId(id).ifPresent(loc -> {
            detail.setLongitude(loc.getLongitude());
            detail.setLatitude(loc.getLatitude());
        });

        int productCount = gasProductRepository.findAllBySellerId(id).size();
        detail.setProductCount(productCount);

        int totalStock = gasStockRepository.sumQuantityBySellerId(id);
        detail.setTotalStock(totalStock);

        return detail;
    }

    public void suspendSeller(Long id) {
        GasSeller seller = gasSellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendeur introuvable"));
        seller.setIsActive(false);
        gasSellerRepository.save(seller);
    }

    public void reactivateSeller(Long id) {
        GasSeller seller = gasSellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendeur introuvable"));
        seller.setIsActive(true);
        gasSellerRepository.save(seller);
    }

    // ─── Statistiques ────────────────────────────────────────────────────────

    public AdminStatsResponse getStats() {
        long totalSellers = gasSellerRepository.count();
        long activeSellers = gasSellerRepository.countByIsActive(true);
        long suspendedSellers = gasSellerRepository.countByIsActive(false);
        long openSellers = gasSellerRepository.countByIsOpen(true);
        long totalProducts = gasProductRepository.count();
        long totalAdmins = adminRepository.count();

        return new AdminStatsResponse()
                .setTotalSellers(totalSellers)
                .setActiveSellers(activeSellers)
                .setSuspendedSellers(suspendedSellers)
                .setOpenSellers(openSellers)
                .setTotalProducts(totalProducts)
                .setTotalAdmins(totalAdmins);
    }

    // ─── Mappers privés ──────────────────────────────────────────────────────

    private AdminResponse toAdminResponse(Admin admin) {
        return new AdminResponse()
                .setId(admin.getId())
                .setName(admin.getName())
                .setEmail(admin.getEmail())
                .setRole(admin.getRole())
                .setIsActive(admin.getIsActive())
                .setCreatedAt(admin.getCreatedAt());
    }

    private AdminSellerSummary toSellerSummary(GasSeller seller) {
        return new AdminSellerSummary()
                .setId(seller.getId())
                .setDisplayName(seller.getDisplayname())
                .setEmail(seller.getEmail())
                .setPhone(seller.getPhone())
                .setIsOpen(seller.getIsOpen())
                .setIsActive(seller.getIsActive())
                .setCreatedAt(seller.getCreatedAt());
    }
}
