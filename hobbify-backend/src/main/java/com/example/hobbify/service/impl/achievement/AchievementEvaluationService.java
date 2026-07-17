package com.example.hobbify.service.impl.achievement;

import com.example.hobbify.bean.core.achievement.Achievement;
import com.example.hobbify.bean.core.step.Step;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.bean.core.user.UserAchievement;
import com.example.hobbify.bean.core.user.UserProgress;
import com.example.hobbify.dao.facade.core.achievement.AchievementDao;
import com.example.hobbify.dao.facade.core.user.UserAchievementDao;
import com.example.hobbify.dao.facade.core.user.UserProgressDao;
import com.example.hobbify.service.facade.step.StepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Awards achievements based on a user's completed-step history. There is no
 * separate "rule engine" — each {@link com.example.hobbify.bean.core.enums.AchievementType}
 * maps to one hard-coded stat computed from {@link UserProgress}, compared against
 * the achievement's threshold. Call {@link #evaluateAndAward(User)} any time a
 * user's progress changes; it is cheap (a handful of in-memory list scans) and
 * idempotent (already-earned achievements are skipped).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementEvaluationService {

    private final UserProgressDao userProgressDao;
    private final AchievementDao achievementDao;
    private final UserAchievementDao userAchievementDao;
    private final StepService stepService;

    @Transactional
    public void evaluateAndAward(final User user) {
        final List<UserProgress> completed = userProgressDao.findAll(
                (root, query, cb) -> cb.and(
                        cb.equal(root.get("user").get("id"), user.getId()),
                        cb.isTrue(root.get("completed"))
                )
        );
        if (completed.isEmpty()) {
            return;
        }

        final Map<Long, Step> stepsById = stepService.findAll().stream()
                .collect(Collectors.toMap(Step::getId, step -> step));

        final int totalCompletedSteps = completed.size();

        final Set<Long> hobbiesStarted = completed.stream()
                .map(progress -> stepsById.get(progress.getStep().getId()))
                .filter(step -> step != null && step.getStage() != null && step.getStage().getHobby() != null)
                .map(step -> step.getStage().getHobby().getId())
                .collect(Collectors.toSet());

        final Set<LocalDate> activeDays = completed.stream()
                .map(progress -> (progress.getCompletedAt() != null ? progress.getCompletedAt() : progress.getCreatedDate()))
                .filter(java.util.Objects::nonNull)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        final long hobbiesFullyCompleted = hobbiesStarted.stream()
                .filter(hobbyId -> isHobbyFullyCompleted(hobbyId, completed, stepsById))
                .count();

        final Set<Long> alreadyEarned = userAchievementDao.findByUser_Id(user.getId()).stream()
                .map(ua -> ua.getAchievement().getId())
                .collect(Collectors.toSet());

        for (final Achievement achievement : achievementDao.findAll()) {
            if (alreadyEarned.contains(achievement.getId())) {
                continue;
            }
            final int threshold = achievement.getThreshold().intValue();
            final boolean qualifies = switch (achievement.getType()) {
                case ONBOARDING, MILESTONE -> totalCompletedSteps >= threshold;
                case EXPLORER -> hobbiesStarted.size() >= threshold;
                case MASTERY -> hobbiesFullyCompleted >= threshold;
                case STREAK -> activeDays.size() >= threshold;
            };
            if (qualifies) {
                final UserAchievement award = UserAchievement.builder()
                        .user(user)
                        .achievement(achievement)
                        .earnedAt(LocalDateTime.now())
                        .build();
                userAchievementDao.save(award);
                log.info("Awarded achievement '{}' to user {}", achievement.getName(), user.getId());
            }
        }
    }

    private boolean isHobbyFullyCompleted(final Long hobbyId, final List<UserProgress> completed, final Map<Long, Step> stepsById) {
        final long totalStepsInHobby = stepsById.values().stream()
                .filter(step -> step.getStage() != null && step.getStage().getHobby() != null)
                .filter(step -> step.getStage().getHobby().getId().equals(hobbyId))
                .count();
        if (totalStepsInHobby == 0) {
            return false;
        }
        final long completedInHobby = completed.stream()
                .map(progress -> stepsById.get(progress.getStep().getId()))
                .filter(step -> step != null && step.getStage() != null && step.getStage().getHobby() != null)
                .filter(step -> step.getStage().getHobby().getId().equals(hobbyId))
                .count();
        return completedInHobby >= totalStepsInHobby;
    }
}
