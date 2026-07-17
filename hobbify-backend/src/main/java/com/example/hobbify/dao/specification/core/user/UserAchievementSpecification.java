package com.example.hobbify.dao.specification.core.user;

import com.example.hobbify.dao.criteria.core.user.UserAchievementCriteria;
import com.example.hobbify.bean.core.user.UserAchievement;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserAchievementSpecification implements Specification<UserAchievement> {

    private final UserAchievementCriteria criteria;
    private final boolean distinct;

    public UserAchievementSpecification(UserAchievementCriteria criteria) {
        this(criteria, false);
    }

    public UserAchievementSpecification(UserAchievementCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<UserAchievement> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (distinct) {
            query.distinct(true);
        }

        // ── Base entity fields ──────────────────────────────────────
        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getRef() != null && !criteria.getRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ref")),
                "%" + criteria.getRef().toLowerCase() + "%"));
        }

        if (criteria.getCreatedAt() != null) {
            predicates.add(cb.equal(root.get("createdDate"), criteria.getCreatedAt()));
        }
        if (criteria.getCreatedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), criteria.getCreatedAtFrom()));
        }
        if (criteria.getCreatedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), criteria.getCreatedAtTo()));
        }

        if (criteria.getUpdatedAt() != null) {
            predicates.add(cb.equal(root.get("lastModifiedDate"), criteria.getUpdatedAt()));
        }
        if (criteria.getUpdatedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastModifiedDate"), criteria.getUpdatedAtFrom()));
        }
        if (criteria.getUpdatedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("lastModifiedDate"), criteria.getUpdatedAtTo()));
        }

        // ── Entity-specific fields ──────────────────────────────────
        // earnedAt - LocalDateTime field (supports range)
        if (criteria.getEarnedAt() != null) {
            predicates.add(cb.equal(root.get("earnedAt"), criteria.getEarnedAt()));
        }
        if (criteria.getEarnedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("earnedAt"), criteria.getEarnedAtFrom()));
        }
        if (criteria.getEarnedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("earnedAt"), criteria.getEarnedAtTo()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // user - ManyToOne relationship
        if (criteria.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), criteria.getUserId()));
        }
        // achievement - ManyToOne relationship
        if (criteria.getAchievementId() != null) {
            predicates.add(cb.equal(root.get("achievement").get("id"), criteria.getAchievementId()));
        }
        if (criteria.getAchievementRef() != null && !criteria.getAchievementRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("achievement").get("ref")),
                "%" + criteria.getAchievementRef().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
