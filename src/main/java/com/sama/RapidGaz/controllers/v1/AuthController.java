package com.sama.RapidGaz.controllers.v1;

import com.sama.RapidGaz.dtos.LoginGasSellerDto;
import com.sama.RapidGaz.dtos.RegisterGasSellerDto;
import com.sama.RapidGaz.model.GasSeller;
import com.sama.RapidGaz.responses.LoginResponse;
import com.sama.RapidGaz.responses.RegisterResponse;
import com.sama.RapidGaz.services.AuthenticationService;
import com.sama.RapidGaz.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    public AuthController(
            JwtService jwtService,
            AuthenticationService authenticationService,
            UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterGasSellerDto registerGasSellerDto
    ) {
        GasSeller registeredGasSeller = authenticationService.signup(registerGasSellerDto);

        RegisterResponse registerResponse = new RegisterResponse()
                .setId(registeredGasSeller.getId())
                .setDisplayname(registeredGasSeller.getDisplayname())
                .setEmail(registeredGasSeller.getEmail())
                .setPhone(registeredGasSeller.getPhone())
                .setIsOpen(registeredGasSeller.getIsOpen());

        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginGasSellerDto loginGasSellerDto
    ) {
        GasSeller authenticatedGasSeller = authenticationService.authenticate(loginGasSellerDto);

        String accessToken = jwtService.generateSellerToken(authenticatedGasSeller);
        String refreshToken = jwtService.generateSellerRefreshToken(authenticatedGasSeller);

        LoginResponse loginResponse = new LoginResponse()
                .setAccessToken(accessToken)
                .setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(refreshToken).toString())
                .body(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        String category = jwtService.extractTokenCategory(refreshToken);
        String type = jwtService.extractTokenType(refreshToken);
        if (!"refresh".equals(category) || !"seller".equals(type)) {
            return ResponseEntity.status(401).build();
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            return ResponseEntity.status(401).build();
        }

        String newAccessToken = jwtService.generateSellerToken(userDetails);
        String newRefreshToken = jwtService.generateSellerRefreshToken(userDetails);

        LoginResponse loginResponse = new LoginResponse()
                .setAccessToken(newAccessToken)
                .setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(newRefreshToken).toString())
                .body(loginResponse);
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
                .path("/api/v1/auth/refresh")
                .maxAge(jwtService.getRefreshExpirationTime() / 1000)
                .sameSite("Strict")
                .build();
    }
}
