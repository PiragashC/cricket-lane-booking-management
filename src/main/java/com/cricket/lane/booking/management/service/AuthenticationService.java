package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.api.dto.AuthRequest;
import com.cricket.lane.booking.management.api.dto.AuthResponse;
import com.cricket.lane.booking.management.api.dto.RefreshTokenRequest;
import com.cricket.lane.booking.management.api.dto.UserDto;
import com.cricket.lane.booking.management.config.JwtService;
import com.cricket.lane.booking.management.config.RefreshTokenService;
import com.cricket.lane.booking.management.entity.RefreshToken;
import com.cricket.lane.booking.management.entity.User;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse authenticate(AuthRequest request) {
        try {
            Authentication authentication = authenticateUser(request.getEmail(), request.getPassword());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ServiceException("User not found", "Bad request", HttpStatus.BAD_REQUEST));

            return buildAuthResponse(user, authentication);

        } catch (BadCredentialsException e) {
            throw new ServiceException("Invalid email or password", "Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new ServiceException("Refresh token not found", "Unauthorized", HttpStatus.UNAUTHORIZED));

        User user = refreshToken.getUser();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, user.getAuthorities());

        String newAccessToken = jwtService.generateToken(user, authentication);
        return buildAuthResponse(user, newAccessToken);
    }

    private AuthResponse buildAuthResponse(User user, Authentication authentication) {
        String jwtToken = jwtService.generateToken(user, authentication);
        return buildAuthResponse(user, jwtToken);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        UserDto userDto = new UserDto();
        userDto.setUserName(user.getFirstName() + " " + user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userDto(userDto)
                .build();
    }

    private Authentication authenticateUser(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
    }
}