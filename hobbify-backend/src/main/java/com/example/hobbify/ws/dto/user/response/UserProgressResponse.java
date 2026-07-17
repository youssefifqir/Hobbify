package com.example.hobbify.ws.dto.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressResponse {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Boolean completed;
    private LocalDateTime completedAt;
    private String userId;
    private Long stepId;
    private String stepRef;
}

