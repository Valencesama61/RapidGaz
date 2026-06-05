package com.sama.RapidGaz.controllers.admin;

import com.sama.RapidGaz.configs.AdminUserDetailsService;
import com.sama.RapidGaz.dtos.AdminLoginRequest;
import com.sama.RapidGaz.model.Admin;
import com.sama.RapidGaz.responses.AdminLoginResponse;
import com.sama.RapidGaz.services.AdminAuthService;
import com.sama.RapidGaz.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final JwtService jwtService;
    private final AdminUserDetailsService adminUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @Valid @RequestBody AdminLoginRequest request
    ) {
        AdminLoginResponse response = adminAuthService.login(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(response.getRefreshToken()).toString())
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AdminLoginResponse> refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        String category = jwtService.extractTokenCategory(refreshToken);
        String type = jwtService.extractTokenType(refreshToken);
        if (!"refresh".equals(category) || !"admin".equals(type)) {
            return ResponseEntity.status(401).build();
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = adminUserDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            return ResponseEntity.status(401).build();
        }

        Admin admin = (Admin) userDetails;

        String newAccessToken = jwtService.generateAdminToken(admin);
        String newRefreshToken = jwtService.generateAdminRefreshToken(admin);

        AdminLoginResponse response = new AdminLoginResponse()
                .setAccessToken(newAccessToken)
                .setTokenType("Bearer")
                .setRole(admin.getRole().name())
                .setName(admin.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(newRefreshToken).toString())
                .body(response);
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private ResponseCookie buildRefreshCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(false) // passer à true en production (HTTPS obligatoire)
                .path("/api/admin/auth/refresh")
                .maxAge(jwtService.getRefreshExpirationTime() / 1000)
                .sameSite("Strict")
                .build();
    }
}
