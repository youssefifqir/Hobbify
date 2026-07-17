package com.example.hobbify.ws.dto.step.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepResponse {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String title;
    private String content;
    private BigDecimal order;
    private BigDecimal estimatedMinutes;
    private Long stageId;
    private String stageRef;
}

