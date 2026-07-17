package com.example.hobbify.dao.specification.core.user;

import com.example.hobbify.dao.criteria.core.user.UserProgressCriteria;
import com.example.hobbify.bean.core.user.UserProgress;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserProgressSpecification implements Specification<UserProgress> {

    private final UserProgressCriteria criteria;
    private final boolean distinct;

    public UserProgressSpecification(UserProgressCriteria criteria) {
        this(criteria, false);
    }

    public UserProgressSpecification(UserProgressCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<UserProgress> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // completed - Boolean field
        if (criteria.getCompleted() != null) {
            predicates.add(cb.equal(root.get("completed"), criteria.getCompleted()));
        }
        // completedAt - LocalDateTime field (supports range)
        if (criteria.getCompletedAt() != null) {
            predicates.add(cb.equal(root.get("completedAt"), criteria.getCompletedAt()));
        }
        if (criteria.getCompletedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("completedAt"), criteria.getCompletedAtFrom()));
        }
        if (criteria.getCompletedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("completedAt"), criteria.getCompletedAtTo()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // user - ManyToOne relationship
        if (criteria.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), criteria.getUserId()));
        }
        // step - ManyToOne relationship
        if (criteria.getStepId() != null) {
            predicates.add(cb.equal(root.get("step").get("id"), criteria.getStepId()));
        }
        if (criteria.getStepRef() != null && !criteria.getStepRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("step").get("ref")),
                "%" + criteria.getStepRef().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
