package com.example.hobbify.service.impl.achievement;

import com.example.hobbify.bean.core.achievement.Achievement;
import com.example.hobbify.dao.criteria.core.achievement.AchievementCriteria;
import com.example.hobbify.dao.facade.core.achievement.AchievementDao;
import com.example.hobbify.dao.specification.core.achievement.AchievementSpecification;
import com.example.hobbify.service.facade.achievement.AchievementService;
import com.example.hobbify.config.security.authz.Authorize.Permit;
import com.example.hobbify.config.security.authz.AuthorizationSpecificationAdvisor;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import com.example.hobbify.common.event.EntityEvent;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class AchievementServiceImpl implements AchievementService {

    private final AchievementDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthorizationSpecificationAdvisor rowFilter;

    public AchievementServiceImpl(AchievementDao dao, ApplicationEventPublisher eventPublisher, AuthorizationSpecificationAdvisor rowFilter) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.rowFilter = rowFilter;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Achievement:READ")
    public List<Achievement> findAll() {
        Specification<Achievement> spec = rowFilter.forList(Achievement.class);
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Achievement:READ")
    public Optional<Achievement> findById(Long id) {
        Specification<Achievement> spec = rowFilter.forList(Achievement.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("id"), id)))
                : dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_achievement", allEntries = true)
    public Achievement save(Achievement entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Achievement entity) {
        if (entity == null) return;

        // Handle OneToMany relationships - set parent reference
        // NOTE: checked for non-empty, not just non-null — the field is always non-null (empty
        // collection literal in the entity), so a plain != null check here misfires on every
        // request that never touches this relationship (e.g. converters that only set scalar
        // fields), wiping/reattaching a collection nothing actually asked to change.
        if (entity.getUserachievements() != null && !entity.getUserachievements().isEmpty()) {
            entity.getUserachievements().forEach(child -> {
                if (child != null) {
                    child.setAchievement(entity);
                }
            });
        }
    }

    private void validateDeletionAllowed(Achievement entity) {
        if (entity == null) return;

        // Keep as an extension point for domain-specific delete guards.
    }

    private void prepareForDeletion(Achievement entity) {
        if (entity == null) return;

        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_achievement", allEntries = true)
    @Permit("Achievement:DELETE")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Achievement", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_achievement", allEntries = true)
    @Permit("Achievement:DELETE")
    public Optional<Achievement> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Achievement", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_achievement", allEntries = true)
    @Permit("Achievement:CREATE")
    public Achievement create(Achievement t) {
        Achievement result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Achievement", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_achievement", allEntries = true)
    @Permit("Achievement:UPDATE")
    public Achievement update(Achievement t) {
        if (t == null || t.getId() == null) return null;
        Achievement existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Achievement result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Achievement", result));
        }
        return result;
    }

    private void mergeEntityData(Achievement existing, Achievement updated) {
        if (existing == null || updated == null) return;

        if (updated.getName() != null) {
            existing.setName(updated.getName().isEmpty() ? null : updated.getName());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().isEmpty() ? null : updated.getDescription());
        }
        if (updated.getType() != null) {
            existing.setType(updated.getType());
        }
        if (updated.getThreshold() != null) {
            existing.setThreshold(updated.getThreshold());
        }
        if (updated.getIconUrl() != null) {
            existing.setIconUrl(updated.getIconUrl().isEmpty() ? null : updated.getIconUrl());
        }

        // Handle relationships
        // NOTE: checked for non-empty, not just non-null — this field is always non-null
        // (initialized to an empty collection on the entity), so a plain != null check fires on
        // every update() call, even DTOs/converters that never populate this relationship at
        // all. That unconditionally clears the real, persisted collection (deleting rows via
        // orphanRemoval) and, worse, can hand Hibernate a still-transient child with no cascade
        // configured on the association, throwing TransientObjectException on flush.
        if (updated.getUserachievements() != null && !updated.getUserachievements().isEmpty()) {
            if (existing.getUserachievements() == null) {
                existing.setUserachievements(new LinkedHashSet<>());
            } else {
                existing.getUserachievements().clear();
            }

            updated.getUserachievements().forEach(child -> {
                if (child != null) {
                    child.setAchievement(existing);
                    existing.getUserachievements().add(child);
                }
            });
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Achievement> update(List<Achievement> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Achievement> result = new ArrayList<>();
        for (Achievement entity : ts) {
            if (entity.getId() != null) {
                Achievement updated = update(entity);
                if (updated != null) {
                    result.add(updated);
                }
            } else if (createIfNotExist) {
                result.add(create(entity));
            }
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    public Achievement findOrSave(Achievement t) { 
        if (t == null) return null;
        
        Achievement existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Achievement findByReferenceEntity(Achievement t) { 
        if (t == null) return null;
        
        if (t.getId() != null) {
            return findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            return findByRef(t.getRef());
        }
        
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Achievement findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Achievement> findByCriteria(AchievementCriteria criteria) {
        Specification<Achievement> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new AchievementSpecification(criteria),
                rowFilter.forList(Achievement.class)
        );
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Achievement> findPaginatedByCriteria(AchievementCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Achievement> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new AchievementSpecification(criteria),
                rowFilter.forList(Achievement.class)
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(AchievementCriteria criteria) {
        Specification<Achievement> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new AchievementSpecification(criteria),
                rowFilter.forList(Achievement.class)
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Achievement> delete(List<Achievement> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Achievement> deleted = new ArrayList<>();
        for (Achievement entity : ts) {
            if (entity != null && entity.getId() != null) {
                findById(entity.getId()).ifPresent(e -> {
                    e.setDeletedAt(LocalDateTime.now());
                    dao.save(e);
                    deleted.add(e);
                });
            }
        }
        return deleted;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Achievement:READ")
    public Achievement findByRef(String ref) {
        if (ref == null || ref.trim().isEmpty()) return null;
        Specification<Achievement> spec = rowFilter.forList(Achievement.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("ref"), ref))).orElse(null)
                : dao.findByRef(ref);
    }

    /**
     * Combines the caller's criteria Specification with the PolicyEngine row filter
     * (AND logic, null-tolerant on both sides).
     */
    private Specification<Achievement> combineSpecs(Specification<Achievement> criteriaSpec,
                                                       Specification<Achievement> authzSpec) {
        if (criteriaSpec == null && authzSpec == null) return null;
        if (criteriaSpec == null) return authzSpec;
        if (authzSpec == null) return criteriaSpec;
        return criteriaSpec.and(authzSpec);
    }
}

