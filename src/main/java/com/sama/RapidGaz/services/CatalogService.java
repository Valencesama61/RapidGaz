package com.sama.RapidGaz.services;

import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.exceptions.RessourceExistException;
import com.sama.RapidGaz.model.CatalogBrand;
import com.sama.RapidGaz.model.CatalogSize;
import com.sama.RapidGaz.repository.CatalogBrandRepository;
import com.sama.RapidGaz.repository.CatalogSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogBrandRepository brandRepository;
    private final CatalogSizeRepository  sizeRepository;

    // ── Brands ──────────────────────────────────────────────────────────────

    public List<CatalogBrand> getBrands() {
        return brandRepository.findAll();
    }

    public CatalogBrand addBrand(String name) {
        if (brandRepository.existsByNameIgnoreCase(name)) {
            throw new RessourceExistException("La marque '" + name + "' existe déjà.");
        }
        return brandRepository.save(new CatalogBrand(name));
    }

    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new NotFoundException("Marque introuvable.");
        }
        brandRepository.deleteById(id);
    }

    // ── Sizes ────────────────────────────────────────────────────────────────

    public List<CatalogSize> getSizes() {
        return sizeRepository.findAll();
    }

    public CatalogSize addSize(String name) {
        if (sizeRepository.existsByNameIgnoreCase(name)) {
            throw new RessourceExistException("La taille '" + name + "' existe déjà.");
        }
        return sizeRepository.save(new CatalogSize(name));
    }

    public void deleteSize(Long id) {
        if (!sizeRepository.existsById(id)) {
            throw new NotFoundException("Taille introuvable.");
        }
        sizeRepository.deleteById(id);
    }

    // ── Helpers pour la validation produit ───────────────────────────────────

    public boolean isBrandValid(String name) {
        return brandRepository.existsByNameIgnoreCase(name);
    }

    public boolean isSizeValid(String name) {
        return sizeRepository.existsByNameIgnoreCase(name);
    }
}
