package com.sama.RapidGaz.configs;

import com.sama.RapidGaz.enums.AdminRole;
import com.sama.RapidGaz.model.Admin;
import com.sama.RapidGaz.model.CatalogBrand;
import com.sama.RapidGaz.model.CatalogSize;
import com.sama.RapidGaz.repository.AdminRepository;
import com.sama.RapidGaz.repository.CatalogBrandRepository;
import com.sama.RapidGaz.repository.CatalogSizeRepository;
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

    private final AdminRepository        adminRepository;
    private final BCryptPasswordEncoder  passwordEncoder;
    private final CatalogBrandRepository brandRepository;
    private final CatalogSizeRepository  sizeRepository;

    @Value("${app.super-admin.email}")    private String superAdminEmail;
    @Value("${app.super-admin.password}") private String superAdminPassword;
    @Value("${app.super-admin.name}")     private String superAdminName;

    @Override
    public void run(String... args) {
        seedSuperAdmin();
        seedCatalog();
    }

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

    private void seedCatalog() {
        List<String> defaultBrands = List.of("ORYX", "JNP", "TOTAL");
        for (String name : defaultBrands) {
            if (!brandRepository.existsByNameIgnoreCase(name)) {
                brandRepository.save(new CatalogBrand(name));
                log.info("Marque ajoutée au catalogue : {}", name);
            }
        }

        List<String> defaultSizes = List.of("KG_6", "KG_12", "KG_25");
        for (String name : defaultSizes) {
            if (!sizeRepository.existsByNameIgnoreCase(name)) {
                sizeRepository.save(new CatalogSize(name));
                log.info("Taille ajoutée au catalogue : {}", name);
            }
        }
    }
}
