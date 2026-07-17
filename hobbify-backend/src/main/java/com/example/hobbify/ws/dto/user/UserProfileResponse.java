package com.example.hobbify.ws.dto.user;

import java.time.LocalDateTime;
import java.util.List;

public record UserProfileResponse(
        String id,
        String email,
        String firstName,
        String lastName,
        List<String> roles,
        LocalDateTime createdDate
) {}
