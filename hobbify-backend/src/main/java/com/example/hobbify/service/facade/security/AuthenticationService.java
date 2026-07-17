package com.example.hobbify.service.facade.security;

import com.example.hobbify.ws.dto.auth.AuthenticationRequest;
import com.example.hobbify.ws.dto.auth.RefreshRequest;
import com.example.hobbify.ws.dto.auth.RegistrationRequest;
import com.example.hobbify.ws.dto.auth.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest req);

    void logout(String authorizationHeader);
}
