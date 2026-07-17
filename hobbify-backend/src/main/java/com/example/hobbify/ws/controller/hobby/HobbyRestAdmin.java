package com.example.hobbify.ws.controller.hobby;

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

import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.dao.criteria.core.hobby.HobbyCriteria;
import com.example.hobbify.service.facade.hobby.HobbyService;
import com.example.hobbify.ws.converter.hobby.HobbyConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.hobby.request.CreateHobbyRequest;
import com.example.hobbify.ws.dto.hobby.request.UpdateHobbyRequest;
import com.example.hobbify.ws.dto.hobby.response.HobbyResponse;

@RestController
@RequestMapping("/api/v1/hobbys")
@PreAuthorize("hasRole('ADMIN')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin — Hobby", description = "Hobby management API (admin)")
public class HobbyRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "name", "description", "category", "costTier", "spaceNeeded", "timeCommitment", "difficulty", "status", "contentSource", "lastReviewedAt"
    );

    private final HobbyService service;
    private final HobbyConverter converter;

    public HobbyRestAdmin(HobbyService service, HobbyConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List Hobby records (paginated)")
    public ResponseEntity<PageResponse<HobbyResponse>> findAll(
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
        final var result = service.findPaginatedByCriteria(new HobbyCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HobbyResponse> findById(@PathVariable Long id) {
        Hobby entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hobby not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new Hobby")
    public ResponseEntity<HobbyResponse> create(@Valid @RequestBody CreateHobbyRequest request) {
        Hobby entity = converter.toEntity(request);
        Hobby created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Hobby")
    public ResponseEntity<HobbyResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateHobbyRequest request) {
        Hobby entity = converter.toEntity(request);
        entity.setId(id);
        Hobby updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Hobby")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hobby not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

