package com.example.hobbify.ws.converter.achievement;

import org.springframework.stereotype.Component;
import com.example.hobbify.bean.core.achievement.Achievement;
import com.example.hobbify.ws.dto.achievement.request.CreateAchievementRequest;
import com.example.hobbify.ws.dto.achievement.request.UpdateAchievementRequest;
import com.example.hobbify.ws.dto.achievement.response.AchievementResponse;

@Component
public class AchievementConverter {

    public AchievementResponse toResponse(Achievement entity) {
        if (entity == null) return null;
        AchievementResponse response = new AchievementResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setType(entity.getType());
        response.setThreshold(entity.getThreshold());
        response.setIconUrl(entity.getIconUrl());
        return response;
    }

    public Achievement toEntity(CreateAchievementRequest request) {
        if (request == null) return null;
        Achievement entity = new Achievement();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setThreshold(request.getThreshold());
        entity.setIconUrl(request.getIconUrl());
        return entity;
    }

    public Achievement toEntity(UpdateAchievementRequest request) {
        if (request == null) return null;
        Achievement entity = new Achievement();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setThreshold(request.getThreshold());
        entity.setIconUrl(request.getIconUrl());
        return entity;
    }
}

