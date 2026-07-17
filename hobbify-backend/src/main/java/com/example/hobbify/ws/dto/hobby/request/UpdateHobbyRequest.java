package com.example.hobbify.ws.dto.hobby.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import com.example.hobbify.bean.core.enums.CostTier;
import com.example.hobbify.bean.core.enums.SpaceNeeded;
import com.example.hobbify.bean.core.enums.TimeCommitment;
import com.example.hobbify.bean.core.enums.Difficulty;
import com.example.hobbify.bean.core.enums.ContentStatus;
import com.example.hobbify.bean.core.enums.ContentSource;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHobbyRequest {

    @Size(max = 500, message = "name must not exceed 500 characters")
    private String name;
    @Size(max = 5000, message = "description must not exceed 5000 characters")
    private String description;
    @Size(max = 500, message = "category must not exceed 500 characters")
    private String category;
    private CostTier costTier;
    private SpaceNeeded spaceNeeded;
    private TimeCommitment timeCommitment;
    private Difficulty difficulty;
    private ContentStatus status;
    private ContentSource contentSource;
    private LocalDateTime lastReviewedAt;
    private String icon;
    private String imageData;
}

