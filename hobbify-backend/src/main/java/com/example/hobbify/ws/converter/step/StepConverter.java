package com.example.hobbify.ws.converter.step;

import org.springframework.stereotype.Component;
import com.example.hobbify.bean.core.step.Step;
import com.example.hobbify.ws.dto.step.request.CreateStepRequest;
import com.example.hobbify.ws.dto.step.request.UpdateStepRequest;
import com.example.hobbify.ws.dto.step.response.StepResponse;
import com.example.hobbify.bean.core.stage.Stage;

@Component
public class StepConverter {

    public StepResponse toResponse(Step entity) {
        if (entity == null) return null;
        StepResponse response = new StepResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setOrder(entity.getOrder());
        response.setEstimatedMinutes(entity.getEstimatedMinutes());
        if (entity.getStage() != null) {
            response.setStageId(entity.getStage().getId());
            response.setStageRef(entity.getStage().getRef());
        }
        return response;
    }

    public Step toEntity(CreateStepRequest request) {
        if (request == null) return null;
        Step entity = new Step();
        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setOrder(request.getOrder());
        entity.setEstimatedMinutes(request.getEstimatedMinutes());
        if (request.getStageId() != null || request.getStageRef() != null) {
            Stage stageRef = new Stage();
            stageRef.setId(request.getStageId());
            stageRef.setRef(request.getStageRef());
            entity.setStage(stageRef);
        }
        return entity;
    }

    public Step toEntity(UpdateStepRequest request) {
        if (request == null) return null;
        Step entity = new Step();
        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setOrder(request.getOrder());
        entity.setEstimatedMinutes(request.getEstimatedMinutes());
        if (request.getStageId() != null || request.getStageRef() != null) {
            Stage stageRef = new Stage();
            stageRef.setId(request.getStageId());
            stageRef.setRef(request.getStageRef());
            entity.setStage(stageRef);
        }
        return entity;
    }
}

