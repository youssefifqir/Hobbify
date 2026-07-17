package com.example.hobbify.ws.dto.achievement.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.hobbify.bean.core.enums.AchievementType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponse {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String name;
    private String description;
    private AchievementType type;
    private BigDecimal threshold;
    private String iconUrl;
}

