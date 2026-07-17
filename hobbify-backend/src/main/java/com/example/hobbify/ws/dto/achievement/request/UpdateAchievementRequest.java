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
public class UpdateAchievementRequest {

    @Size(max = 500, message = "name must not exceed 500 characters")
    private String name;
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    private AchievementType type;
    @DecimalMin(value = "0.0", inclusive = true, message = "threshold must be positive")
    private BigDecimal threshold;
    @Size(max = 500, message = "iconUrl must not exceed 500 characters")
    private String iconUrl;
}

