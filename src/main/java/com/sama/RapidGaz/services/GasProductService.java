package com.sama.RapidGaz.services;

import com.sama.RapidGaz.dtos.GasProductDto;
import com.sama.RapidGaz.dtos.UpdateGasProductDto;
import com.sama.RapidGaz.exceptions.NotAuthorizedException;
import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.exceptions.RessourceExistException;
import com.sama.RapidGaz.mappers.GasProductMapper;
import com.sama.RapidGaz.model.GasProduct;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.model.GasStock;
import com.sama.RapidGaz.repository.GasProductRepository;
import com.sama.RapidGaz.repository.GasSellerRepository;
import com.sama.RapidGaz.repository.GasStockRepository;
import com.sama.RapidGaz.responses.GasProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GasProductService {

    private final GasProductRepository productRepository;
    private final GasSellerRepository  sellerRepository;
    private final GasStockRepository   stockRepository;
    private final CatalogService       catalogService;

    private Integer getStock(Long sellerId, Long productId) {
        return stockRepository.findBySellerIdAndProductId(sellerId, productId)
                .map(GasStock::getQuantity)
                .orElse(0);
    }

    private void validateCatalog(String brand, String size) {
        if (brand != null && !catalogService.isBrandValid(brand)) {
            throw new IllegalArgumentException("Marque inconnue : " + brand);
        }
        if (size != null && !catalogService.isSizeValid(size)) {
            throw new IllegalArgumentException("Taille inconnue : " + size);
        }
    }

    @Transactional
    public GasProductResponse create(Long sellerId, GasProductDto input) {
        GasSeller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("Vendeur introuvable"));

        validateCatalog(input.getBrand(), input.getSize());

        productRepository.findByBrandAndSizeAndSellerId(input.getBrand(), input.getSize(), sellerId)
                .ifPresent(p -> { throw new RessourceExistException(
                        "Un produit " + input.getBrand() + " " + input.getSize() + " existe déjà."); });

        GasProduct product = new GasProduct()
                .setSeller(seller)
                .setBrand(input.getBrand())
                .setSize(input.getSize())
                .setPrice(input.getPrice());

        GasProduct saved = productRepository.save(product);

        GasStock stock = new GasStock();
        stock.setProduct(saved);
        stock.setSeller(seller);
        stock.setQuantity(0);
        stockRepository.save(stock);

        return GasProductMapper.toResponse(saved, 0);
    }

    public List<GasProductResponse> getAll(Long sellerId) {
        return productRepository.findAllBySellerId(sellerId).stream()
                .map(p -> GasProductMapper.toResponse(p, getStock(sellerId, p.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GasProductResponse getOne(Long id, Long sellerId) {
        GasProduct product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit introuvable"));
        if (!Objects.equals(product.getSeller().getId(), sellerId)) {
            throw new NotAuthorizedException("Ce produit ne vous appartient pas");
        }
        return GasProductMapper.toResponse(product, getStock(sellerId, id));
    }

    @Transactional
    public GasProductResponse update(Long sellerId, Long productId, UpdateGasProductDto input) {
        GasProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produit introuvable"));
        if (!Objects.equals(product.getSeller().getId(), sellerId)) {
            throw new NotAuthorizedException("Ce produit ne vous appartient pas");
        }
        validateCatalog(input.getBrand(), input.getSize());
        if (input.getBrand() != null) product.setBrand(input.getBrand());
        if (input.getSize()  != null) product.setSize(input.getSize());
        if (input.getPrice() != null) product.setPrice(input.getPrice());
        productRepository.save(product);
        return GasProductMapper.toResponse(product, getStock(sellerId, productId));
    }

    @Transactional
    public void remove(Long productId, Long sellerId) {
        GasProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produit introuvable"));
        if (!Objects.equals(product.getSeller().getId(), sellerId)) {
            throw new NotAuthorizedException("Ce produit ne vous appartient pas");
        }
        stockRepository.deleteByProductId(product.getId());
        productRepository.delete(product);
    }
}
