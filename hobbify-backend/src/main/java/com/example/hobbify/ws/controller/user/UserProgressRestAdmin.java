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

import com.example.hobbify.bean.core.user.UserProgress;
import com.example.hobbify.dao.criteria.core.user.UserProgressCriteria;
import com.example.hobbify.service.facade.user.UserProgressService;
import com.example.hobbify.ws.converter.user.UserProgressConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.user.request.CreateUserProgressRequest;
import com.example.hobbify.ws.dto.user.request.UpdateUserProgressRequest;
import com.example.hobbify.ws.dto.user.response.UserProgressResponse;

@RestController
@RequestMapping("/api/v1/admin/userprogresss")
@PreAuthorize("hasRole('ADMIN')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin — UserProgress", description = "UserProgress management API (admin)")
public class UserProgressRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "completed", "completedAt"
    );

    private final UserProgressService service;
    private final UserProgressConverter converter;

    public UserProgressRestAdmin(UserProgressService service, UserProgressConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List UserProgress records (paginated)")
    public ResponseEntity<PageResponse<UserProgressResponse>> findAll(
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
        final var result = service.findPaginatedByCriteria(new UserProgressCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProgressResponse> findById(@PathVariable Long id) {
        UserProgress entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserProgress not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new UserProgress")
    public ResponseEntity<UserProgressResponse> create(@Valid @RequestBody CreateUserProgressRequest request) {
        UserProgress entity = converter.toEntity(request);
        UserProgress created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing UserProgress")
    public ResponseEntity<UserProgressResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUserProgressRequest request) {
        UserProgress entity = converter.toEntity(request);
        entity.setId(id);
        UserProgress updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a UserProgress")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserProgress not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

