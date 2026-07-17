package com.example.hobbify.ws.controller.favorite;

import com.example.hobbify.bean.core.favorite.Favorite;
import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.dao.facade.core.favorite.FavoriteDao;
import com.example.hobbify.service.facade.hobby.HobbyService;
import com.example.hobbify.ws.converter.favorite.FavoriteConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.favorite.request.CreateFavoriteRequest;
import com.example.hobbify.ws.dto.favorite.response.FavoriteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Favorites", description = "User favorites management")
public class FavoriteController {

    private final FavoriteDao favoriteDao;
    private final FavoriteConverter favoriteConverter;
    private final HobbyService hobbyService;

    @GetMapping
    @Operation(summary = "List current user's favorites (paginated)")
    public ResponseEntity<PageResponse<FavoriteResponse>> getFavorites(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") @Max(200) int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Specification<Favorite> spec = (root, query, cb) ->
                cb.equal(root.get("user").get("id"), user.getId());
        int effectiveSize = Math.min(size, 200);
        var pageable = PageRequest.of(page, effectiveSize,
                "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var result = favoriteDao.findAll(spec, pageable)
                .map(favoriteConverter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PostMapping
    @Operation(summary = "Add a favorite for the current user")
    public ResponseEntity<FavoriteResponse> addFavorite(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateFavoriteRequest request) {
        Favorite entity = favoriteConverter.toEntity(request);
        entity.setUser(user);
        if (entity.getHobby() != null && entity.getHobby().getId() != null) {
            Hobby hobby = hobbyService.findById(entity.getHobby().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Hobby not found with id: " + entity.getHobby().getId()));
            entity.setHobby(hobby);
        }
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        Favorite saved = favoriteDao.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteConverter.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a favorite by id (only if owned by current user)")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        Specification<Favorite> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("id"), id),
                cb.equal(root.get("user").get("id"), user.getId())
        );
        Favorite favorite = favoriteDao.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + id));
        favorite.setDeletedAt(LocalDateTime.now());
        favoriteDao.save(favorite);
        return ResponseEntity.noContent().build();
    }
}
