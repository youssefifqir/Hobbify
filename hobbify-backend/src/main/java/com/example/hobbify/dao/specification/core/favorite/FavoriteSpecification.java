package com.example.hobbify.dao.specification.core.favorite;

import com.example.hobbify.dao.criteria.core.favorite.FavoriteCriteria;
import com.example.hobbify.bean.core.favorite.Favorite;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSpecification implements Specification<Favorite> {

    private final FavoriteCriteria criteria;
    private final boolean distinct;

    public FavoriteSpecification(FavoriteCriteria criteria) {
        this(criteria, false);
    }

    public FavoriteSpecification(FavoriteCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Favorite> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

        // ── Relationship fields (foreign key lookups) ───────────────
        // user - ManyToOne relationship
        if (criteria.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), criteria.getUserId()));
        }
        // hobby - ManyToOne relationship
        if (criteria.getHobbyId() != null) {
            predicates.add(cb.equal(root.get("hobby").get("id"), criteria.getHobbyId()));
        }
        if (criteria.getHobbyRef() != null && !criteria.getHobbyRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("hobby").get("ref")),
                "%" + criteria.getHobbyRef().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
