package com.sama.RapidGaz.services;

import com.sama.RapidGaz.dtos.GasSellerChangePasswordDto;
import com.sama.RapidGaz.dtos.UpdateGasSellerDto;
import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.repository.GasSellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GasSellerService {

    private final GasSellerRepository gasSellerRepository;
    private final PasswordEncoder passwordEncoder;


    public GasSeller update(GasSeller current, UpdateGasSellerDto input) {

        if (input.getDisplayName() != null && !input.getDisplayName().isEmpty()) {
            current.setDisplayname(input.getDisplayName());
        }
        if(input.getPhone()!=null && !input.getPhone().isEmpty()) {
            current.setPhone(input.getPhone());
        }
        if(input.getIsOpen()!=null) {
            current.setIsOpen(input.getIsOpen());
        }

        return gasSellerRepository.save(current);

    }

    public void changePass(String email, GasSellerChangePasswordDto input) {
        GasSeller gasSeller = gasSellerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Vendeur introuvable"));

        if(!passwordEncoder.matches(input.getOldPassword(), gasSeller.getPassword())){
            throw new IllegalArgumentException("L'ancien mot de passe ne correspond pas");
        }
        gasSeller.setPassword(passwordEncoder.encode(input.getNewPassword()));
        gasSellerRepository.save(gasSeller);

    }

}

