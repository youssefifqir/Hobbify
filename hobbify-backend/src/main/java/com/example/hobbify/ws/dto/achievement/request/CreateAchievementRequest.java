package com.example.hobbify.ws.dto.achievement.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import com.example.hobbify.bean.core.enums.AchievementType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAchievementRequest {

    @NotBlank(message = "name is required")
    @Size(max = 500, message = "name must not exceed 500 characters")
    private String name;
    @NotBlank(message = "description is required")
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    private AchievementType type;
    @NotNull(message = "threshold is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "threshold must be positive")
    private BigDecimal threshold;
    @Size(max = 500, message = "iconUrl must not exceed 500 characters")
    private String iconUrl;
}

