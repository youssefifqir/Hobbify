package com.example.hobbify.ws.converter.favorite;

import org.springframework.stereotype.Component;
import com.example.hobbify.bean.core.favorite.Favorite;
import com.example.hobbify.ws.dto.favorite.request.CreateFavoriteRequest;
import com.example.hobbify.ws.dto.favorite.request.UpdateFavoriteRequest;
import com.example.hobbify.ws.dto.favorite.response.FavoriteResponse;
import com.example.hobbify.bean.core.hobby.Hobby;

@Component
public class FavoriteConverter {

    public FavoriteResponse toResponse(Favorite entity) {
        if (entity == null) return null;
        FavoriteResponse response = new FavoriteResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setCreatedAt(entity.getCreatedAt());
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getHobby() != null) {
            response.setHobbyId(entity.getHobby().getId());
            response.setHobbyRef(entity.getHobby().getRef());
        }
        return response;
    }

    public Favorite toEntity(CreateFavoriteRequest request) {
        if (request == null) return null;
        Favorite entity = new Favorite();
        entity.setCreatedAt(request.getCreatedAt());
        if (request.getHobbyId() != null || request.getHobbyRef() != null) {
            Hobby hobbyRef = new Hobby();
            hobbyRef.setId(request.getHobbyId());
            hobbyRef.setRef(request.getHobbyRef());
            entity.setHobby(hobbyRef);
        }
        return entity;
    }

    public Favorite toEntity(UpdateFavoriteRequest request) {
        if (request == null) return null;
        Favorite entity = new Favorite();
        entity.setCreatedAt(request.getCreatedAt());
        if (request.getHobbyId() != null || request.getHobbyRef() != null) {
            Hobby hobbyRef = new Hobby();
            hobbyRef.setId(request.getHobbyId());
            hobbyRef.setRef(request.getHobbyRef());
            entity.setHobby(hobbyRef);
        }
        return entity;
    }
}

