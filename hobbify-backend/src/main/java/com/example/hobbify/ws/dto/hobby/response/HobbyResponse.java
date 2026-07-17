package com.example.hobbify.ws.dto.hobby.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class HobbyResponse {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String name;
    private String description;
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

