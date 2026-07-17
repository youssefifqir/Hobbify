package com.example.hobbify.dao.specification.core.step;

import com.example.hobbify.dao.criteria.core.step.StepCriteria;
import com.example.hobbify.bean.core.step.Step;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StepSpecification implements Specification<Step> {

    private final StepCriteria criteria;
    private final boolean distinct;

    public StepSpecification(StepCriteria criteria) {
        this(criteria, false);
    }

    public StepSpecification(StepCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Step> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // title - String field (supports like search)
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")),
                "%" + criteria.getTitle().toLowerCase() + "%"));
        }
        if (criteria.getTitleLike() != null && !criteria.getTitleLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")),
                "%" + criteria.getTitleLike().toLowerCase() + "%"));
        }
        // content - String field (supports like search)
        if (criteria.getContent() != null && !criteria.getContent().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("content")),
                "%" + criteria.getContent().toLowerCase() + "%"));
        }
        if (criteria.getContentLike() != null && !criteria.getContentLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("content")),
                "%" + criteria.getContentLike().toLowerCase() + "%"));
        }
        // order - BigDecimal field (supports range)
        if (criteria.getOrder() != null) {
            predicates.add(cb.equal(root.get("order"), criteria.getOrder()));
        }
        if (criteria.getOrderMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("order"), criteria.getOrderMin()));
        }
        if (criteria.getOrderMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("order"), criteria.getOrderMax()));
        }
        // estimatedMinutes - BigDecimal field (supports range)
        if (criteria.getEstimatedMinutes() != null) {
            predicates.add(cb.equal(root.get("estimatedMinutes"), criteria.getEstimatedMinutes()));
        }
        if (criteria.getEstimatedMinutesMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("estimatedMinutes"), criteria.getEstimatedMinutesMin()));
        }
        if (criteria.getEstimatedMinutesMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("estimatedMinutes"), criteria.getEstimatedMinutesMax()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // stage - ManyToOne relationship
        if (criteria.getStageId() != null) {
            predicates.add(cb.equal(root.get("stage").get("id"), criteria.getStageId()));
        }
        if (criteria.getStageRef() != null && !criteria.getStageRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("stage").get("ref")),
                "%" + criteria.getStageRef().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
