package com.example.hobbify.ws.dto.stage.request;

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
public class UpdateStageRequest {

    @Size(max = 500, message = "title must not exceed 500 characters")
    private String title;
    @DecimalMin(value = "0.0", inclusive = true, message = "order must be positive")
    private BigDecimal order;

    /** References an existing Hobby by id or ref. */
    private Long hobbyId;
    private String hobbyRef;
}

