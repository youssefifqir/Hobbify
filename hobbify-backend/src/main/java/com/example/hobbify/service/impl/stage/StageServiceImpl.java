package com.example.hobbify.service.impl.stage;

import com.example.hobbify.bean.core.stage.Stage;
import com.example.hobbify.dao.criteria.core.stage.StageCriteria;
import com.example.hobbify.dao.facade.core.stage.StageDao;
import com.example.hobbify.dao.specification.core.stage.StageSpecification;
import com.example.hobbify.service.facade.stage.StageService;
import com.example.hobbify.service.facade.hobby.HobbyService;
import com.example.hobbify.bean.core.hobby.Hobby;
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
public class StageServiceImpl implements StageService {

    private final StageDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthorizationSpecificationAdvisor rowFilter;
    private final HobbyService hobbyService;

    public StageServiceImpl(StageDao dao, ApplicationEventPublisher eventPublisher, AuthorizationSpecificationAdvisor rowFilter, @Lazy HobbyService hobbyService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.rowFilter = rowFilter;
        this.hobbyService = hobbyService;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Stage:READ")
    public List<Stage> findAll() {
        Specification<Stage> spec = rowFilter.forList(Stage.class);
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Stage:READ")
    public Optional<Stage> findById(Long id) {
        Specification<Stage> spec = rowFilter.forList(Stage.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("id"), id)))
                : dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_stage", allEntries = true)
    public Stage save(Stage entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Stage entity) {
        if (entity == null) return;

        // Validate ManyToOne relationships exist
        if (entity.getHobby() != null) {
            Hobby hobbyEntity = entity.getHobby();
            if (hobbyEntity.getId() != null) {
                Hobby existingHobby = hobbyService.findById(hobbyEntity.getId()).orElse(null);
                if (existingHobby == null) {
                    throw new IllegalArgumentException("Hobby with id " + hobbyEntity.getId() + " does not exist");
                }
                entity.setHobby(existingHobby);
            } else if (hobbyEntity.getRef() != null) {
                Hobby existingHobby = hobbyService.findByRef(hobbyEntity.getRef());
                if (existingHobby == null) {
                    throw new IllegalArgumentException("Hobby with ref '" + hobbyEntity.getRef() + "' does not exist");
                }
                entity.setHobby(existingHobby);
            } else {
                throw new IllegalArgumentException("Hobby must be referenced by id or ref");
            }
        }
        // Handle OneToMany relationships - set parent reference
        // NOTE: checked for non-empty, not just non-null — the field is always non-null (empty
        // collection literal in the entity), so a plain != null check here misfires on every
        // request that never touches this relationship (e.g. converters that only set scalar
        // fields), wiping/reattaching a collection nothing actually asked to change.
        if (entity.getSteps() != null && !entity.getSteps().isEmpty()) {
            entity.getSteps().forEach(child -> {
                if (child != null) {
                    child.setStage(entity);
                }
            });
        }
    }

    private void validateDeletionAllowed(Stage entity) {
        if (entity == null) return;

        // Keep as an extension point for domain-specific delete guards.
    }

    private void prepareForDeletion(Stage entity) {
        if (entity == null) return;

        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_stage", allEntries = true)
    @Permit("Stage:DELETE")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Stage", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_stage", allEntries = true)
    @Permit("Stage:DELETE")
    public Optional<Stage> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Stage", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_stage", allEntries = true)
    @Permit("Stage:CREATE")
    public Stage create(Stage t) {
        Stage result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Stage", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_stage", allEntries = true)
    @Permit("Stage:UPDATE")
    public Stage update(Stage t) {
        if (t == null || t.getId() == null) return null;
        Stage existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Stage result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Stage", result));
        }
        return result;
    }

    private void mergeEntityData(Stage existing, Stage updated) {
        if (existing == null || updated == null) return;

        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle().isEmpty() ? null : updated.getTitle());
        }
        if (updated.getOrder() != null) {
            existing.setOrder(updated.getOrder());
        }

        // Handle relationships
        if (updated.getHobby() != null) {
            existing.setHobby(updated.getHobby());
        }
        // NOTE: checked for non-empty, not just non-null — this field is always non-null
        // (initialized to an empty collection on the entity), so a plain != null check fires on
        // every update() call, even DTOs/converters that never populate this relationship at
        // all. That unconditionally clears the real, persisted collection (deleting rows via
        // orphanRemoval) and, worse, can hand Hibernate a still-transient child with no cascade
        // configured on the association, throwing TransientObjectException on flush.
        if (updated.getSteps() != null && !updated.getSteps().isEmpty()) {
            if (existing.getSteps() == null) {
                existing.setSteps(new LinkedHashSet<>());
            } else {
                existing.getSteps().clear();
            }

            updated.getSteps().forEach(child -> {
                if (child != null) {
                    child.setStage(existing);
                    existing.getSteps().add(child);
                }
            });
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Stage> update(List<Stage> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Stage> result = new ArrayList<>();
        for (Stage entity : ts) {
            if (entity.getId() != null) {
                Stage updated = update(entity);
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
    public Stage findOrSave(Stage t) { 
        if (t == null) return null;
        
        Stage existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Stage findByReferenceEntity(Stage t) { 
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
    public Stage findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stage> findByCriteria(StageCriteria criteria) {
        Specification<Stage> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new StageSpecification(criteria),
                rowFilter.forList(Stage.class)
        );
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Stage> findPaginatedByCriteria(StageCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Stage> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new StageSpecification(criteria),
                rowFilter.forList(Stage.class)
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(StageCriteria criteria) {
        Specification<Stage> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new StageSpecification(criteria),
                rowFilter.forList(Stage.class)
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Stage> delete(List<Stage> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Stage> deleted = new ArrayList<>();
        for (Stage entity : ts) {
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
    @Permit("Stage:READ")
    public Stage findByRef(String ref) {
        if (ref == null || ref.trim().isEmpty()) return null;
        Specification<Stage> spec = rowFilter.forList(Stage.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("ref"), ref))).orElse(null)
                : dao.findByRef(ref);
    }

    /**
     * Combines the caller's criteria Specification with the PolicyEngine row filter
     * (AND logic, null-tolerant on both sides).
     */
    private Specification<Stage> combineSpecs(Specification<Stage> criteriaSpec,
                                                       Specification<Stage> authzSpec) {
        if (criteriaSpec == null && authzSpec == null) return null;
        if (criteriaSpec == null) return authzSpec;
        if (authzSpec == null) return criteriaSpec;
        return criteriaSpec.and(authzSpec);
    }
}

