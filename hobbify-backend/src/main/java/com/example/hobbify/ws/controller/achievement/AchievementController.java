package com.example.hobbify.ws.controller.achievement;

import com.example.hobbify.bean.core.achievement.Achievement;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.bean.core.user.UserAchievement;
import com.example.hobbify.dao.criteria.core.achievement.AchievementCriteria;
import com.example.hobbify.dao.facade.core.achievement.AchievementDao;
import com.example.hobbify.dao.facade.core.user.UserAchievementDao;
import com.example.hobbify.service.facade.achievement.AchievementService;
import com.example.hobbify.ws.converter.achievement.AchievementConverter;
import com.example.hobbify.ws.dto.PageResponse;
import com.example.hobbify.ws.dto.achievement.response.AchievementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
@Tag(name = "Achievements", description = "Achievement catalog")
public class AchievementController {

    private final AchievementService achievementService;
    private final AchievementConverter achievementConverter;
    private final AchievementDao achievementDao;
    private final UserAchievementDao userAchievementDao;

    @GetMapping
    @Operation(summary = "List all achievements (paginated)")
    public ResponseEntity<PageResponse<AchievementResponse>> getAchievements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") @Max(200) int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        int effectiveSize = Math.min(size, 200);
        var pageable = PageRequest.of(page, effectiveSize,
                "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var result = achievementService.findPaginatedByCriteria(new AchievementCriteria(), pageable)
                .map(achievementConverter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/mine")
    @Operation(summary = "List every achievement with the current user's earned status")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<MyAchievementResponse>> getMyAchievements(@AuthenticationPrincipal final User user) {
        final Map<Long, UserAchievement> earnedByAchievementId = userAchievementDao.findByUser_Id(user.getId()).stream()
                .collect(Collectors.toMap(ua -> ua.getAchievement().getId(), ua -> ua));

        final List<MyAchievementResponse> response = achievementDao.findAll().stream()
                .sorted(Comparator.comparing(Achievement::getName))
                .map(achievement -> {
                    final UserAchievement earned = earnedByAchievementId.get(achievement.getId());
                    return new MyAchievementResponse(
                            achievement.getId(), achievement.getName(), achievement.getDescription(),
                            achievement.getType(), achievement.getThreshold(), achievement.getIconUrl(),
                            earned != null, earned != null ? earned.getEarnedAt() : null);
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    public record MyAchievementResponse(
            Long id, String name, String description,
            com.example.hobbify.bean.core.enums.AchievementType type, java.math.BigDecimal threshold, String iconUrl,
            boolean earned, LocalDateTime earnedAt
    ) {}
}
