package com.sama.RapidGaz.configs;

import com.sama.RapidGaz.enums.AdminRole;
import com.sama.RapidGaz.model.*;
import com.sama.RapidGaz.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository              adminRepository;
    private final BCryptPasswordEncoder        passwordEncoder;
    private final CatalogBrandRepository       brandRepository;
    private final CatalogSizeRepository        sizeRepository;
    private final GasSellerRepository          sellerRepository;
    private final GasSellerLocationRepository  locationRepository;
    private final GasProductRepository         productRepository;
    private final GasStockRepository           stockRepository;

    @Value("${app.super-admin.email}")    private String superAdminEmail;
    @Value("${app.super-admin.password}") private String superAdminPassword;
    @Value("${app.super-admin.name}")     private String superAdminName;

    @Override
    public void run(String... args) {
        seedSuperAdmin();
        seedCatalog();
        seedSampleSellers();
    }

    // -------------------------------------------------------------------------
    // Super Admin
    // -------------------------------------------------------------------------

    private void seedSuperAdmin() {
        adminRepository.findByRole(AdminRole.SUPER_ADMIN).ifPresentOrElse(
            existing -> {
                existing.setEmail(superAdminEmail)
                        .setName(superAdminName)
                        .setPassword(passwordEncoder.encode(superAdminPassword));
                adminRepository.save(existing);
                log.info("SUPER_ADMIN mis à jour : {}", superAdminEmail);
            },
            () -> {
                Admin superAdmin = new Admin()
                        .setEmail(superAdminEmail)
                        .setPassword(passwordEncoder.encode(superAdminPassword))
                        .setName(superAdminName)
                        .setRole(AdminRole.SUPER_ADMIN)
                        .setIsActive(true);
                adminRepository.save(superAdmin);
                log.info("SUPER_ADMIN créé : {}", superAdminEmail);
            }
        );
    }

    // -------------------------------------------------------------------------
    // Catalogue
    // -------------------------------------------------------------------------

    private void seedCatalog() {
        List<String> defaultBrands = List.of("ORYX", "JNP", "TOTAL");
        for (String name : defaultBrands) {
            if (!brandRepository.existsByNameIgnoreCase(name)) {
                brandRepository.save(new CatalogBrand(name));
                log.info("Marque ajoutée : {}", name);
            }
        }

        List<String> defaultSizes = List.of("KG_6", "KG_12", "KG_25");
        for (String name : defaultSizes) {
            if (!sizeRepository.existsByNameIgnoreCase(name)) {
                sizeRepository.save(new CatalogSize(name));
                log.info("Taille ajoutée : {}", name);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Vendeurs de démonstration (ignoré si déjà présents)
    // -------------------------------------------------------------------------

    private record SellerSeed(
        String name, String email, String phone,
        double lat, double lon, boolean isOpen
    ) {}

    private record ProductSeed(String brand, String size, double price, int qty) {}

    private void seedSampleSellers() {
        if (sellerRepository.count() > 0) {
            log.info("Vendeurs déjà présents — seed ignoré");
            return;
        }

        String defaultPassword = passwordEncoder.encode("Vendeur123!");

        List<SellerSeed> sellers = List.of(
            new SellerSeed("Kofi Gaz",       "kofi.gaz@rapidgaz.bj",       "+22961234501", 6.3654,  2.3158,  true),
            new SellerSeed("Ama Distribution","ama.dist@rapidgaz.bj",       "+22961234502", 6.3720,  2.3210,  true),
            new SellerSeed("Sèna Énergie",   "sena.energie@rapidgaz.bj",   "+22961234503", 6.3580,  2.3090,  false),
            new SellerSeed("Dodji Combustibles","dodji.comb@rapidgaz.bj",  "+22961234504", 6.3800,  2.3350,  true),
            new SellerSeed("Akosua Gaz",     "akosua.gaz@rapidgaz.bj",     "+22961234505", 6.3450,  2.3050,  true),
            new SellerSeed("Mensah Services","mensah.serv@rapidgaz.bj",    "+22961234506", 6.3900,  2.3400,  false),
            new SellerSeed("Yawa Énergie",   "yawa.energie@rapidgaz.bj",   "+22961234507", 6.3610,  2.2980,  true),
            new SellerSeed("Komi Gaz Plus",  "komi.gazplus@rapidgaz.bj",   "+22961234508", 6.3700,  2.3500,  true),
            new SellerSeed("Abla Distribution","abla.dist@rapidgaz.bj",    "+22961234509", 6.3530,  2.3250,  false),
            new SellerSeed("Efo Combustibles","efo.comb@rapidgaz.bj",      "+22961234510", 6.3840,  2.3120,  true)
        );

        List<List<ProductSeed>> products = List.of(
            List.of(new ProductSeed("ORYX",  "KG_6",  3500,  8), new ProductSeed("ORYX",  "KG_12", 6500, 5)),
            List.of(new ProductSeed("JNP",   "KG_6",  3400,  10), new ProductSeed("JNP",  "KG_25", 12000, 3)),
            List.of(new ProductSeed("TOTAL", "KG_12", 6800,  6), new ProductSeed("TOTAL", "KG_25", 13000, 2)),
            List.of(new ProductSeed("ORYX",  "KG_6",  3500,  12), new ProductSeed("ORYX", "KG_12", 6600, 4), new ProductSeed("ORYX", "KG_25", 12500, 2)),
            List.of(new ProductSeed("JNP",   "KG_6",  3450,  9), new ProductSeed("JNP",   "KG_12", 6700, 3)),
            List.of(new ProductSeed("TOTAL", "KG_6",  3600,  7), new ProductSeed("TOTAL", "KG_12", 6900, 5)),
            List.of(new ProductSeed("ORYX",  "KG_12", 6500,  8), new ProductSeed("JNP",   "KG_25", 11500, 4)),
            List.of(new ProductSeed("JNP",   "KG_6",  3400,  15), new ProductSeed("JNP",  "KG_12", 6600, 6), new ProductSeed("JNP", "KG_25", 12000, 3)),
            List.of(new ProductSeed("TOTAL", "KG_6",  3550,  5), new ProductSeed("TOTAL", "KG_25", 13500, 1)),
            List.of(new ProductSeed("ORYX",  "KG_6",  3500,  11), new ProductSeed("ORYX", "KG_12", 6400, 7))
        );

        for (int i = 0; i < sellers.size(); i++) {
            SellerSeed s = sellers.get(i);

            GasSeller seller = new GasSeller()
                    .setDisplayname(s.name())
                    .setEmail(s.email())
                    .setPhone(s.phone())
                    .setPassword(defaultPassword)
                    .setIsOpen(s.isOpen())
                    .setIsActive(true);
            sellerRepository.save(seller);

            GasSellerLocation location = new GasSellerLocation()
                    .setLatitude(s.lat())
                    .setLongitude(s.lon())
                    .setSeller(seller);
            locationRepository.save(location);

            for (ProductSeed p : products.get(i)) {
                GasProduct product = new GasProduct()
                        .setBrand(p.brand())
                        .setSize(p.size())
                        .setPrice(p.price())
                        .setSeller(seller);
                productRepository.save(product);

                GasStock stock = new GasStock()
                        .setQuantity(p.qty())
                        .setSeller(seller)
                        .setProduct(product);
                stockRepository.save(stock);
            }

            log.info("Vendeur seedé : {}", s.name());
        }

        log.info("10 vendeurs de démonstration créés (mot de passe : Vendeur123!)");
    }
}
