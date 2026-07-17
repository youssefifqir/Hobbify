package com.example.hobbify.ws.dto.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserAchievementRequest {

    private LocalDateTime earnedAt;

    /** References an existing Achievement by id or ref. */
    private Long achievementId;
    private String achievementRef;
}

