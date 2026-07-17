package com.example.hobbify.ws.dto.favorite.request;

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
public class CreateFavoriteRequest {

    private LocalDateTime createdAt;

    /** References an existing Hobby by id or ref. */
    private Long hobbyId;
    private String hobbyRef;
}

