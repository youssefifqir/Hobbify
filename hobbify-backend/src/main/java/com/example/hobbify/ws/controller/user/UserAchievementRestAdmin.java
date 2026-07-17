package com.example.hobbify.ws.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import com.example.hobbify.bean.core.user.UserAchievement;
import com.example.hobbify.dao.criteria.core.user.UserAchievementCriteria;
import com.example.hobbify.service.facade.user.UserAchievementService;
import com.example.hobbify.ws.converter.user.UserAchievementConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.user.request.CreateUserAchievementRequest;
import com.example.hobbify.ws.dto.user.request.UpdateUserAchievementRequest;
import com.example.hobbify.ws.dto.user.response.UserAchievementResponse;

@RestController
@RequestMapping("/api/v1/admin/userachievements")
@PreAuthorize("hasRole('ADMIN')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin — UserAchievement", description = "UserAchievement management API (admin)")
public class UserAchievementRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "earnedAt"
    );

    private final UserAchievementService service;
    private final UserAchievementConverter converter;

    public UserAchievementRestAdmin(UserAchievementService service, UserAchievementConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List UserAchievement records (paginated)")
    public ResponseEntity<PageResponse<UserAchievementResponse>> findAll(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") @Max(200) final int size,
            @RequestParam(defaultValue = "createdDate") final String sortBy,
            @RequestParam(defaultValue = "desc") final String sortDir) {
        if (!ALLOWED_SORT_COLUMNS.contains(sortBy)) {
            return ResponseEntity.badRequest().body(null);
        }
        if (!"asc".equalsIgnoreCase(sortDir) && !"desc".equalsIgnoreCase(sortDir)) {
            return ResponseEntity.badRequest().body(null);
        }
        final int effectiveSize = Math.min(size, 200);
        final var pageable = PageRequest.of(page, effectiveSize,
                "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        final var result = service.findPaginatedByCriteria(new UserAchievementCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAchievementResponse> findById(@PathVariable Long id) {
        UserAchievement entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserAchievement not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new UserAchievement")
    public ResponseEntity<UserAchievementResponse> create(@Valid @RequestBody CreateUserAchievementRequest request) {
        UserAchievement entity = converter.toEntity(request);
        UserAchievement created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing UserAchievement")
    public ResponseEntity<UserAchievementResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUserAchievementRequest request) {
        UserAchievement entity = converter.toEntity(request);
        entity.setId(id);
        UserAchievement updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a UserAchievement")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserAchievement not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

