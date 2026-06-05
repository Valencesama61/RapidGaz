package com.sama.RapidGaz.services;

import com.sama.RapidGaz.dtos.AdminLoginRequest;
import com.sama.RapidGaz.model.Admin;
import com.sama.RapidGaz.repository.AdminRepository;
import com.sama.RapidGaz.responses.AdminLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminLoginResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email ou password incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BadCredentialsException("Email ou password incorrect");
        }

        if (!Boolean.TRUE.equals(admin.getIsActive())) {
            throw new DisabledException("Ce compte admin est désactivé");
        }

        String accessToken = jwtService.generateAdminToken(admin);
        String refreshToken = jwtService.generateAdminRefreshToken(admin);

        return new AdminLoginResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setTokenType("Bearer")
                .setRole(admin.getRole().name())
                .setName(admin.getName());
    }
}
