package com.example.hobbify.ws.controller.user;

import com.example.hobbify.bean.core.step.Step;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.bean.core.user.UserProgress;
import com.example.hobbify.dao.facade.core.user.UserProgressDao;
import com.example.hobbify.service.facade.step.StepService;
import com.example.hobbify.service.impl.achievement.AchievementEvaluationService;
import com.example.hobbify.ws.converter.user.UserProgressConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.user.request.CreateUserProgressRequest;
import com.example.hobbify.ws.dto.user.request.UpdateUserProgressRequest;
import com.example.hobbify.ws.dto.user.response.UserProgressResponse;
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

@RestController
@RequestMapping("/api/v1/userprogresss")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Progress", description = "User progress management")
public class UserProgressController {

    private final UserProgressDao userProgressDao;
    private final UserProgressConverter userProgressConverter;
    private final StepService stepService;
    private final AchievementEvaluationService achievementEvaluationService;

    @GetMapping
    @Operation(summary = "List current user's progress (paginated)")
    public ResponseEntity<PageResponse<UserProgressResponse>> getProgress(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") @Max(200) int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Specification<UserProgress> spec = (root, query, cb) ->
                cb.equal(root.get("user").get("id"), user.getId());
        int effectiveSize = Math.min(size, 200);
        var pageable = PageRequest.of(page, effectiveSize,
                "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var result = userProgressDao.findAll(spec, pageable)
                .map(userProgressConverter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PostMapping
    @Operation(summary = "Create a progress entry for the current user")
    public ResponseEntity<UserProgressResponse> createProgress(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateUserProgressRequest request) {
        UserProgress entity = userProgressConverter.toEntity(request);
        entity.setUser(user);
        if (entity.getStep() != null && entity.getStep().getId() != null) {
            Step step = stepService.findById(entity.getStep().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Step not found with id: " + entity.getStep().getId()));
            entity.setStep(step);
        }
        if (entity.getCompleted() == null) {
            entity.setCompleted(false);
        }
        UserProgress saved = userProgressDao.save(entity);
        if (Boolean.TRUE.equals(saved.getCompleted())) {
            achievementEvaluationService.evaluateAndAward(user);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProgressConverter.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a progress entry by id (only if owned by current user)")
    public ResponseEntity<UserProgressResponse> updateProgress(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateUserProgressRequest request) {
        Specification<UserProgress> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("id"), id),
                cb.equal(root.get("user").get("id"), user.getId())
        );
        UserProgress existing = userProgressDao.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("UserProgress not found with id: " + id));

        if (request.getCompleted() != null) {
            existing.setCompleted(request.getCompleted());
        }
        if (request.getCompletedAt() != null) {
            existing.setCompletedAt(request.getCompletedAt());
        }
        if (request.getStepId() != null) {
            Step step = stepService.findById(request.getStepId())
                    .orElseThrow(() -> new IllegalArgumentException("Step not found with id: " + request.getStepId()));
            existing.setStep(step);
        }

        UserProgress saved = userProgressDao.save(existing);
        if (Boolean.TRUE.equals(saved.getCompleted())) {
            achievementEvaluationService.evaluateAndAward(user);
        }
        return ResponseEntity.ok(userProgressConverter.toResponse(saved));
    }
}
