package com.example.hobbify.ws.converter.user;

import org.springframework.stereotype.Component;
import com.example.hobbify.bean.core.user.UserAchievement;
import com.example.hobbify.ws.dto.user.request.CreateUserAchievementRequest;
import com.example.hobbify.ws.dto.user.request.UpdateUserAchievementRequest;
import com.example.hobbify.ws.dto.user.response.UserAchievementResponse;
import com.example.hobbify.bean.core.achievement.Achievement;

@Component
public class UserAchievementConverter {

    public UserAchievementResponse toResponse(UserAchievement entity) {
        if (entity == null) return null;
        UserAchievementResponse response = new UserAchievementResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setEarnedAt(entity.getEarnedAt());
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getAchievement() != null) {
            response.setAchievementId(entity.getAchievement().getId());
            response.setAchievementRef(entity.getAchievement().getRef());
        }
        return response;
    }

    public UserAchievement toEntity(CreateUserAchievementRequest request) {
        if (request == null) return null;
        UserAchievement entity = new UserAchievement();
        entity.setEarnedAt(request.getEarnedAt());
        if (request.getAchievementId() != null || request.getAchievementRef() != null) {
            Achievement achievementRef = new Achievement();
            achievementRef.setId(request.getAchievementId());
            achievementRef.setRef(request.getAchievementRef());
            entity.setAchievement(achievementRef);
        }
        return entity;
    }

    public UserAchievement toEntity(UpdateUserAchievementRequest request) {
        if (request == null) return null;
        UserAchievement entity = new UserAchievement();
        entity.setEarnedAt(request.getEarnedAt());
        if (request.getAchievementId() != null || request.getAchievementRef() != null) {
            Achievement achievementRef = new Achievement();
            achievementRef.setId(request.getAchievementId());
            achievementRef.setRef(request.getAchievementRef());
            entity.setAchievement(achievementRef);
        }
        return entity;
    }
}

