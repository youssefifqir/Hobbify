package com.example.hobbify.ws.controller.favorite;

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

import com.example.hobbify.bean.core.favorite.Favorite;
import com.example.hobbify.dao.criteria.core.favorite.FavoriteCriteria;
import com.example.hobbify.service.facade.favorite.FavoriteService;
import com.example.hobbify.ws.converter.favorite.FavoriteConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.favorite.request.CreateFavoriteRequest;
import com.example.hobbify.ws.dto.favorite.request.UpdateFavoriteRequest;
import com.example.hobbify.ws.dto.favorite.response.FavoriteResponse;

@RestController
@RequestMapping("/api/v1/admin/favorites")
@PreAuthorize("hasRole('ADMIN')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin — Favorite", description = "Favorite management API (admin)")
public class FavoriteRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "createdAt"
    );

    private final FavoriteService service;
    private final FavoriteConverter converter;

    public FavoriteRestAdmin(FavoriteService service, FavoriteConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List Favorite records (paginated)")
    public ResponseEntity<PageResponse<FavoriteResponse>> findAll(
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
        final var result = service.findPaginatedByCriteria(new FavoriteCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> findById(@PathVariable Long id) {
        Favorite entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new Favorite")
    public ResponseEntity<FavoriteResponse> create(@Valid @RequestBody CreateFavoriteRequest request) {
        Favorite entity = converter.toEntity(request);
        Favorite created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Favorite")
    public ResponseEntity<FavoriteResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateFavoriteRequest request) {
        Favorite entity = converter.toEntity(request);
        entity.setId(id);
        Favorite updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Favorite")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

