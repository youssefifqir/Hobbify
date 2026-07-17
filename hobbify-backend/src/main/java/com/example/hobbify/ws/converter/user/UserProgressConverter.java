package com.example.hobbify.ws.converter.user;

import org.springframework.stereotype.Component;
import com.example.hobbify.bean.core.user.UserProgress;
import com.example.hobbify.ws.dto.user.request.CreateUserProgressRequest;
import com.example.hobbify.ws.dto.user.request.UpdateUserProgressRequest;
import com.example.hobbify.ws.dto.user.response.UserProgressResponse;
import com.example.hobbify.bean.core.step.Step;

@Component
public class UserProgressConverter {

    public UserProgressResponse toResponse(UserProgress entity) {
        if (entity == null) return null;
        UserProgressResponse response = new UserProgressResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setCompleted(entity.getCompleted());
        response.setCompletedAt(entity.getCompletedAt());
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getStep() != null) {
            response.setStepId(entity.getStep().getId());
            response.setStepRef(entity.getStep().getRef());
        }
        return response;
    }

    public UserProgress toEntity(CreateUserProgressRequest request) {
        if (request == null) return null;
        UserProgress entity = new UserProgress();
        entity.setCompleted(request.getCompleted());
        entity.setCompletedAt(request.getCompletedAt());
        if (request.getStepId() != null || request.getStepRef() != null) {
            Step stepRef = new Step();
            stepRef.setId(request.getStepId());
            stepRef.setRef(request.getStepRef());
            entity.setStep(stepRef);
        }
        return entity;
    }

    public UserProgress toEntity(UpdateUserProgressRequest request) {
        if (request == null) return null;
        UserProgress entity = new UserProgress();
        entity.setCompleted(request.getCompleted());
        entity.setCompletedAt(request.getCompletedAt());
        if (request.getStepId() != null || request.getStepRef() != null) {
            Step stepRef = new Step();
            stepRef.setId(request.getStepId());
            stepRef.setRef(request.getStepRef());
            entity.setStep(stepRef);
        }
        return entity;
    }
}

