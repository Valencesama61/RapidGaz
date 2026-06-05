package com.sama.RapidGaz.services;
import com.sama.RapidGaz.dtos.*;
import com.sama.RapidGaz.exceptions.NotFoundException;
import com.sama.RapidGaz.exceptions.RessourceExistException;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.repository.GasSellerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    private final GasSellerRepository gasSellerRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            GasSellerRepository gasSellerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.gasSellerRepository = gasSellerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public GasSeller signup(RegisterGasSellerDto input) {
        if (gasSellerRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RessourceExistException("Cet email est déjà utilisé par un autre vendeur.");
        }
        GasSeller gasSeller = new GasSeller()
                .setDisplayname(input.getDisplayname())
                .setEmail(input.getEmail())
                .setPhone(input.getPhone())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        return gasSellerRepository.save(gasSeller);
    }

    public GasSeller authenticate(LoginGasSellerDto input) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return gasSellerRepository.findByEmail(input.getEmail())
                .orElseThrow(() ->
                        new NotFoundException("Vendeur introuvable")
                );
    }
}
