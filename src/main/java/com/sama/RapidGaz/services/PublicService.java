package com.sama.RapidGaz.services;

import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.mappers.GasProductMapper;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.repository.GasProductRepository;
import com.sama.RapidGaz.repository.GasSellerRepository;
import com.sama.RapidGaz.responses.GasProductResponse;
import com.sama.RapidGaz.responses.ProductInResult;
import com.sama.RapidGaz.responses.PublicSellerDetailResponse;
import com.sama.RapidGaz.responses.SearchResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicService {
    private final GasSellerRepository gasSellerRepository;
    private final GasProductRepository gasProductRepository;
    private static final double DEFAULT_RADIUS = 2.0;

    public List<SearchResultResponse> searchSellers(
            Double latitude,
            Double longitude,
            Double radius,
            String brand,
            String size) {

        //définir le rayon de recherche
        double searchRadius = (radius != null) ? radius : DEFAULT_RADIUS;

        //récupérer les vendeurs ouvert et actif  avec le bon produit
        List<Object[]> rows = gasSellerRepository.findTop3NearbyWithProduct(
                latitude, longitude, searchRadius, brand, size
        );

        // Mapping des résultats bruts vers les DTOs de réponse
        return rows.stream()
                .map(this::mapToResponse)
                .toList();

    }

    public PublicSellerDetailResponse getSellerDetail(Long id) {
        GasSeller seller = gasSellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendeur introuvable"));

        List<GasProductResponse> products = gasProductRepository.findAllBySellerId(id)
                .stream()
                .map(GasProductMapper::toResponse)
                .toList();

        return new PublicSellerDetailResponse()
                .setDisplayName(seller.getDisplayname())
                .setPhone(seller.getPhone())
                .setIsOpen(seller.getIsOpen())
                .setProducts(products);
    }

    private SearchResultResponse mapToResponse(Object[] row) {
        String displayName  = (String) row[0];
        String phone        = (String) row[1];
        Double lat          = (Double) row[2];
        Double lng          = (Double) row[3];
        String brand        = (String) row[4];
        String size         = (String) row[5];
        Double price        = (Double) row[6];
        Integer quantity    = ((Number) row[7]).intValue();
        Double distance     = ((Number) row[8]).doubleValue();

        String googleMapsUrl = "https://www.google.com/maps?q=" + lat + "," + lng;

        ProductInResult product = new ProductInResult()
                .setBrand(brand)
                .setSize(size)
                .setPrice(price)
                .setStockQuantity(quantity);

        return new SearchResultResponse()
                .setDisplayName(displayName)
                .setPhone(phone)
                .setDistance(Math.round(distance * 10.0) / 10.0)
                .setGoogleMapsUrl(googleMapsUrl)
                .setProduct(product);
    }
}
