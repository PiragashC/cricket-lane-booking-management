package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.api.dto.AuthRequest;
import com.cricket.lane.booking.management.api.dto.AuthResponse;
import com.cricket.lane.booking.management.api.dto.RefreshTokenRequest;
import com.cricket.lane.booking.management.config.JwtService;
import com.cricket.lane.booking.management.config.RefreshTokenService;
import com.cricket.lane.booking.management.entity.RefreshToken;
import com.cricket.lane.booking.management.entity.User;
import com.cricket.lane.booking.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse authenticate(AuthRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .userName(user.getFirstName() + " " + user.getLastName())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found in database!"));
    }
}
