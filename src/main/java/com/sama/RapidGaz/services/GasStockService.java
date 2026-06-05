package com.sama.RapidGaz.services;

import com.sama.RapidGaz.dtos.GasStockDto;
import com.sama.RapidGaz.exceptions.NotAuthorizedException;
import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.mappers.GasStockMapper;
import com.sama.RapidGaz.model.GasStock;
import com.sama.RapidGaz.repository.GasStockRepository;
import com.sama.RapidGaz.responses.GasStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GasStockService {
    private final GasStockRepository gasStockRepository;

    @Transactional(readOnly = true)
    public GasStockResponse getStock(Long productId, Long sellerId) {
        GasStock stock = gasStockRepository.findBySellerIdAndProductId(sellerId, productId)
                .orElseThrow(() -> new NotFoundException("Aucun stock trouvé pour ce produit"));

        return GasStockMapper.toResponse(stock);
    }

    @Transactional
    public GasStockResponse update(Long productId, Long sellerId, GasStockDto input) {
        GasStock stock = gasStockRepository.findBySellerIdAndProductId(sellerId, productId)
                .orElseThrow(() -> new NotFoundException("Aucun stock trouvé pour ce produit"));

        stock.setQuantity(input.getQuantity());
        gasStockRepository.save(stock);

        return GasStockMapper.toResponse(stock);
    }

    @Transactional
    public GasStockResponse increment(Long productId, Long sellerId) {
        GasStock stock = gasStockRepository.findBySellerIdAndProductId(sellerId, productId)
                .orElseThrow(() -> new NotFoundException("Aucun stock trouvé pour ce produit"));

        stock.setQuantity(stock.getQuantity() + 1);
        gasStockRepository.save(stock);

        return GasStockMapper.toResponse(stock);
    }

    @Transactional
    public GasStockResponse decrement(Long productId, Long sellerId) {
        GasStock stock = gasStockRepository.findBySellerIdAndProductId(sellerId, productId)
                .orElseThrow(() -> new NotFoundException("Aucun stock trouvé pour ce produit"));

        if (stock.getQuantity() <= 0) {
            throw new NotAuthorizedException("Vous ne pouvez pas mettre le stock en dessous de 0");
        }

        stock.setQuantity(stock.getQuantity() - 1);
        gasStockRepository.save(stock);

        return GasStockMapper.toResponse(stock);
    }

    @Transactional
    public GasStockResponse reset(Long productId, Long sellerId) {
        GasStock stock = gasStockRepository.findBySellerIdAndProductId(sellerId, productId)
                .orElseThrow(() -> new NotFoundException("Aucun stock trouvé pour ce produit"));

        stock.setQuantity(0);
        gasStockRepository.save(stock);

        return GasStockMapper.toResponse(stock);
    }
}
