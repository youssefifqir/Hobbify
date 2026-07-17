package com.example.hobbify.ws.controller.achievement;

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

import com.example.hobbify.bean.core.achievement.Achievement;
import com.example.hobbify.dao.criteria.core.achievement.AchievementCriteria;
import com.example.hobbify.service.facade.achievement.AchievementService;
import com.example.hobbify.ws.converter.achievement.AchievementConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.achievement.request.CreateAchievementRequest;
import com.example.hobbify.ws.dto.achievement.request.UpdateAchievementRequest;
import com.example.hobbify.ws.dto.achievement.response.AchievementResponse;

@RestController
@RequestMapping("/api/v1/admin/achievements")
@PreAuthorize("hasRole('ADMIN')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin — Achievement", description = "Achievement management API (admin)")
public class AchievementRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "name", "description", "type", "threshold", "iconUrl"
    );

    private final AchievementService service;
    private final AchievementConverter converter;

    public AchievementRestAdmin(AchievementService service, AchievementConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List Achievement records (paginated)")
    public ResponseEntity<PageResponse<AchievementResponse>> findAll(
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
        final var result = service.findPaginatedByCriteria(new AchievementCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponse> findById(@PathVariable Long id) {
        Achievement entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new Achievement")
    public ResponseEntity<AchievementResponse> create(@Valid @RequestBody CreateAchievementRequest request) {
        Achievement entity = converter.toEntity(request);
        Achievement created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Achievement")
    public ResponseEntity<AchievementResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateAchievementRequest request) {
        Achievement entity = converter.toEntity(request);
        entity.setId(id);
        Achievement updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Achievement")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

