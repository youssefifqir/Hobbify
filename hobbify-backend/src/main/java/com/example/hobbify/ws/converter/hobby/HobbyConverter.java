package com.example.hobbify.ws.converter.hobby;

import org.springframework.stereotype.Component;
import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.ws.dto.hobby.request.CreateHobbyRequest;
import com.example.hobbify.ws.dto.hobby.request.UpdateHobbyRequest;
import com.example.hobbify.ws.dto.hobby.response.HobbyResponse;

@Component
public class HobbyConverter {

    public HobbyResponse toResponse(Hobby entity) {
        if (entity == null) return null;
        HobbyResponse response = new HobbyResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCategory(entity.getCategory());
        response.setCostTier(entity.getCostTier());
        response.setSpaceNeeded(entity.getSpaceNeeded());
        response.setTimeCommitment(entity.getTimeCommitment());
        response.setDifficulty(entity.getDifficulty());
        response.setStatus(entity.getStatus());
        response.setContentSource(entity.getContentSource());
        response.setLastReviewedAt(entity.getLastReviewedAt());
        response.setIcon(entity.getIcon());
        response.setImageData(entity.getImageData());
        return response;
    }

    public Hobby toEntity(CreateHobbyRequest request) {
        if (request == null) return null;
        Hobby entity = new Hobby();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setCategory(request.getCategory());
        entity.setCostTier(request.getCostTier());
        entity.setSpaceNeeded(request.getSpaceNeeded());
        entity.setTimeCommitment(request.getTimeCommitment());
        entity.setDifficulty(request.getDifficulty());
        entity.setStatus(request.getStatus());
        entity.setContentSource(request.getContentSource());
        entity.setLastReviewedAt(request.getLastReviewedAt());
        entity.setIcon(request.getIcon());
        entity.setImageData(request.getImageData());
        return entity;
    }

    public Hobby toEntity(UpdateHobbyRequest request) {
        if (request == null) return null;
        Hobby entity = new Hobby();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setCategory(request.getCategory());
        entity.setCostTier(request.getCostTier());
        entity.setSpaceNeeded(request.getSpaceNeeded());
        entity.setTimeCommitment(request.getTimeCommitment());
        entity.setDifficulty(request.getDifficulty());
        entity.setStatus(request.getStatus());
        entity.setContentSource(request.getContentSource());
        entity.setLastReviewedAt(request.getLastReviewedAt());
        entity.setIcon(request.getIcon());
        entity.setImageData(request.getImageData());
        return entity;
    }
}

