package com.example.hobbify.ws.dto.step.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStepRequest {

    @Size(max = 500, message = "title must not exceed 500 characters")
    private String title;
    @Size(max = 5000, message = "content must not exceed 5000 characters")
    private String content;
    @DecimalMin(value = "0.0", inclusive = true, message = "order must be positive")
    private BigDecimal order;
    @DecimalMin(value = "0.0", inclusive = true, message = "estimatedMinutes must be positive")
    private BigDecimal estimatedMinutes;

    /** References an existing Stage by id or ref. */
    private Long stageId;
    private String stageRef;
}

