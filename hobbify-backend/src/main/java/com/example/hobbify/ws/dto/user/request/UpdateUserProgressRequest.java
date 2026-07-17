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
public class UpdateUserProgressRequest {

    private Boolean completed;
    private LocalDateTime completedAt;

    /** References an existing Step by id or ref. */
    private Long stepId;
    private String stepRef;
}

