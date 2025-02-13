package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.api.dto.AuthRequest;
import com.cricket.lane.booking.management.api.dto.AuthResponse;
import com.cricket.lane.booking.management.api.dto.RefreshTokenRequest;
import com.cricket.lane.booking.management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
}