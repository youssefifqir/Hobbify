package com.example.hobbify.ws.controller.auth;

import com.example.hobbify.service.facade.security.AuthenticationService;
import com.example.hobbify.ws.dto.auth.AuthenticationRequest;
import com.example.hobbify.ws.dto.auth.RefreshRequest;
import com.example.hobbify.ws.dto.auth.RegistrationRequest;
import com.example.hobbify.ws.dto.auth.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Void> register(@Valid @RequestBody final RegistrationRequest request) {
        this.authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT tokens")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody final AuthenticationRequest request) {
        return ResponseEntity.ok(this.authenticationService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<AuthenticationResponse> refresh(@Valid @RequestBody final RefreshRequest request) {
        return ResponseEntity.ok(this.authenticationService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoke the current access token", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) final String authHeader) {
        this.authenticationService.logout(authHeader);
        return ResponseEntity.noContent().build();
    }
}
