package com.example.hobbify.ws.converter.stage;

import org.springframework.stereotype.Component;
import com.example.hobbify.bean.core.stage.Stage;
import com.example.hobbify.ws.dto.stage.request.CreateStageRequest;
import com.example.hobbify.ws.dto.stage.request.UpdateStageRequest;
import com.example.hobbify.ws.dto.stage.response.StageResponse;
import com.example.hobbify.bean.core.hobby.Hobby;

@Component
public class StageConverter {

    public StageResponse toResponse(Stage entity) {
        if (entity == null) return null;
        StageResponse response = new StageResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setTitle(entity.getTitle());
        response.setOrder(entity.getOrder());
        if (entity.getHobby() != null) {
            response.setHobbyId(entity.getHobby().getId());
            response.setHobbyRef(entity.getHobby().getRef());
        }
        return response;
    }

    public Stage toEntity(CreateStageRequest request) {
        if (request == null) return null;
        Stage entity = new Stage();
        entity.setTitle(request.getTitle());
        entity.setOrder(request.getOrder());
        if (request.getHobbyId() != null || request.getHobbyRef() != null) {
            Hobby hobbyRef = new Hobby();
            hobbyRef.setId(request.getHobbyId());
            hobbyRef.setRef(request.getHobbyRef());
            entity.setHobby(hobbyRef);
        }
        return entity;
    }

    public Stage toEntity(UpdateStageRequest request) {
        if (request == null) return null;
        Stage entity = new Stage();
        entity.setTitle(request.getTitle());
        entity.setOrder(request.getOrder());
        if (request.getHobbyId() != null || request.getHobbyRef() != null) {
            Hobby hobbyRef = new Hobby();
            hobbyRef.setId(request.getHobbyId());
            hobbyRef.setRef(request.getHobbyRef());
            entity.setHobby(hobbyRef);
        }
        return entity;
    }
}

