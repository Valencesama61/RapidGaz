package com.sama.RapidGaz.services;

import com.sama.RapidGaz.dtos.GasSellerLocationDto;
import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.exceptions.RessourceExistException;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.model.GasSellerLocation;
import com.sama.RapidGaz.repository.GasSellerLocationRepository;
import com.sama.RapidGaz.repository.GasSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GasSellerLocationService {
    private final GasSellerLocationRepository gasSellerLocationRepository;
    private final GasSellerRepository gasSellerRepository;

    @Transactional
    public GasSellerLocation addLocation(Long gasSellerId, GasSellerLocationDto input) {
        GasSeller gasSeller = gasSellerRepository.findById(gasSellerId)
                .orElseThrow(() -> new NotFoundException("Le vendeur avec l'identifiant " + gasSellerId + " n'existe pas"));

        //vérifier si une localisation existe déja pour le vendeur
        gasSellerLocationRepository.findBySellerId(gasSellerId)
                .ifPresent(location -> {throw new RessourceExistException("Une localisation existe déja pour ce vendeur");
                });
        GasSellerLocation gasSellerLocation = new GasSellerLocation();
        gasSellerLocation.setSeller(gasSeller);
        gasSellerLocation.setLatitude(input.getLatitude());
        gasSellerLocation.setLongitude(input.getLongitude());

        return gasSellerLocationRepository.save(gasSellerLocation);
    }

    @Transactional
    public GasSellerLocation updateLocation(Long id, GasSellerLocationDto input) {
        GasSeller gasSeller = gasSellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Le vendeur avec l'identifiant " + id + " n'existe pas"));
        GasSellerLocation gasSellerLocation = gasSellerLocationRepository.findBySellerId(id)
                .orElseThrow(() -> new NotFoundException("Aucune donnée à mettre à jour pour ce vendeur"));

        gasSellerLocation.setSeller(gasSeller);
        gasSellerLocation.setLatitude(input.getLatitude());
        gasSellerLocation.setLongitude(input.getLongitude());

        return gasSellerLocationRepository.save(gasSellerLocation);
    }

    @Transactional(readOnly = true)
    public GasSellerLocation myLocation(Long id) {
        gasSellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Le vendeur avec l'identifiant " + id + " n'existe pas"));
        return gasSellerLocationRepository.findBySellerId(id)
                .orElseThrow(() -> new NotFoundException("Aucune localisation disponible pour ce vendeur"));
    }
}
