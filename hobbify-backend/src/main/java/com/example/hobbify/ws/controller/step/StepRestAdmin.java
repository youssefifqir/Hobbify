package com.example.hobbify.ws.controller.step;

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

import com.example.hobbify.bean.core.step.Step;
import com.example.hobbify.dao.criteria.core.step.StepCriteria;
import com.example.hobbify.service.facade.step.StepService;
import com.example.hobbify.ws.converter.step.StepConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.step.request.CreateStepRequest;
import com.example.hobbify.ws.dto.step.request.UpdateStepRequest;
import com.example.hobbify.ws.dto.step.response.StepResponse;

@RestController
@RequestMapping("/api/v1/steps")
@PreAuthorize("hasRole('ADMIN')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Admin — Step", description = "Step management API (admin)")
public class StepRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "title", "content", "order", "estimatedMinutes"
    );

    private final StepService service;
    private final StepConverter converter;

    public StepRestAdmin(StepService service, StepConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List Step records (paginated)")
    public ResponseEntity<PageResponse<StepResponse>> findAll(
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
        final var result = service.findPaginatedByCriteria(new StepCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StepResponse> findById(@PathVariable Long id) {
        Step entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Step not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new Step")
    public ResponseEntity<StepResponse> create(@Valid @RequestBody CreateStepRequest request) {
        Step entity = converter.toEntity(request);
        Step created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Step")
    public ResponseEntity<StepResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateStepRequest request) {
        Step entity = converter.toEntity(request);
        entity.setId(id);
        Step updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Step")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Step not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

