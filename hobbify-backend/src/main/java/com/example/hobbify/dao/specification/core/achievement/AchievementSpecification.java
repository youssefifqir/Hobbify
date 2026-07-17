package com.example.hobbify.dao.specification.core.achievement;

import com.example.hobbify.dao.criteria.core.achievement.AchievementCriteria;
import com.example.hobbify.bean.core.achievement.Achievement;
import com.example.hobbify.bean.core.enums.AchievementType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AchievementSpecification implements Specification<Achievement> {

    private final AchievementCriteria criteria;
    private final boolean distinct;

    public AchievementSpecification(AchievementCriteria criteria) {
        this(criteria, false);
    }

    public AchievementSpecification(AchievementCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Achievement> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // name - String field (supports like search)
        if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                "%" + criteria.getName().toLowerCase() + "%"));
        }
        if (criteria.getNameLike() != null && !criteria.getNameLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                "%" + criteria.getNameLike().toLowerCase() + "%"));
        }
        // description - String field (supports like search)
        if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                "%" + criteria.getDescription().toLowerCase() + "%"));
        }
        if (criteria.getDescriptionLike() != null && !criteria.getDescriptionLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                "%" + criteria.getDescriptionLike().toLowerCase() + "%"));
        }
        // type - Enum field
        if (criteria.getType() != null) {
            predicates.add(cb.equal(root.get("type"), criteria.getType()));
        }
        // threshold - BigDecimal field (supports range)
        if (criteria.getThreshold() != null) {
            predicates.add(cb.equal(root.get("threshold"), criteria.getThreshold()));
        }
        if (criteria.getThresholdMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("threshold"), criteria.getThresholdMin()));
        }
        if (criteria.getThresholdMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("threshold"), criteria.getThresholdMax()));
        }
        // iconUrl - String field (supports like search)
        if (criteria.getIconUrl() != null && !criteria.getIconUrl().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("iconUrl")),
                "%" + criteria.getIconUrl().toLowerCase() + "%"));
        }
        if (criteria.getIconUrlLike() != null && !criteria.getIconUrlLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("iconUrl")),
                "%" + criteria.getIconUrlLike().toLowerCase() + "%"));
        }

        // ── Relationship fields (foreign key lookups) ───────────────

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
