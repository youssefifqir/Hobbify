package com.example.hobbify.dao.specification.core.hobby;

import com.example.hobbify.dao.criteria.core.hobby.HobbyCriteria;
import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.bean.core.enums.CostTier;
import com.example.hobbify.bean.core.enums.SpaceNeeded;
import com.example.hobbify.bean.core.enums.TimeCommitment;
import com.example.hobbify.bean.core.enums.Difficulty;
import com.example.hobbify.bean.core.enums.ContentStatus;
import com.example.hobbify.bean.core.enums.ContentSource;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HobbySpecification implements Specification<Hobby> {

    private final HobbyCriteria criteria;
    private final boolean distinct;

    public HobbySpecification(HobbyCriteria criteria) {
        this(criteria, false);
    }

    public HobbySpecification(HobbyCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Hobby> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // category - String field (supports like search)
        if (criteria.getCategory() != null && !criteria.getCategory().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("category")),
                "%" + criteria.getCategory().toLowerCase() + "%"));
        }
        if (criteria.getCategoryLike() != null && !criteria.getCategoryLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("category")),
                "%" + criteria.getCategoryLike().toLowerCase() + "%"));
        }
        // costTier - Enum field
        if (criteria.getCostTier() != null) {
            predicates.add(cb.equal(root.get("costTier"), criteria.getCostTier()));
        }
        // spaceNeeded - Enum field
        if (criteria.getSpaceNeeded() != null) {
            predicates.add(cb.equal(root.get("spaceNeeded"), criteria.getSpaceNeeded()));
        }
        // timeCommitment - Enum field
        if (criteria.getTimeCommitment() != null) {
            predicates.add(cb.equal(root.get("timeCommitment"), criteria.getTimeCommitment()));
        }
        // difficulty - Enum field
        if (criteria.getDifficulty() != null) {
            predicates.add(cb.equal(root.get("difficulty"), criteria.getDifficulty()));
        }
        // status - Enum field
        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }
        // contentSource - Enum field
        if (criteria.getContentSource() != null) {
            predicates.add(cb.equal(root.get("contentSource"), criteria.getContentSource()));
        }
        // lastReviewedAt - LocalDateTime field (supports range)
        if (criteria.getLastReviewedAt() != null) {
            predicates.add(cb.equal(root.get("lastReviewedAt"), criteria.getLastReviewedAt()));
        }
        if (criteria.getLastReviewedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastReviewedAt"), criteria.getLastReviewedAtFrom()));
        }
        if (criteria.getLastReviewedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("lastReviewedAt"), criteria.getLastReviewedAtTo()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
