package com.example.hobbify.service.impl.step;

import com.example.hobbify.bean.core.step.Step;
import com.example.hobbify.dao.criteria.core.step.StepCriteria;
import com.example.hobbify.dao.facade.core.step.StepDao;
import com.example.hobbify.dao.specification.core.step.StepSpecification;
import com.example.hobbify.service.facade.step.StepService;
import com.example.hobbify.service.facade.stage.StageService;
import com.example.hobbify.bean.core.stage.Stage;
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
public class StepServiceImpl implements StepService {

    private final StepDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthorizationSpecificationAdvisor rowFilter;
    private final StageService stageService;

    public StepServiceImpl(StepDao dao, ApplicationEventPublisher eventPublisher, AuthorizationSpecificationAdvisor rowFilter, @Lazy StageService stageService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.rowFilter = rowFilter;
        this.stageService = stageService;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Step:READ")
    public List<Step> findAll() {
        Specification<Step> spec = rowFilter.forList(Step.class);
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Step:READ")
    public Optional<Step> findById(Long id) {
        Specification<Step> spec = rowFilter.forList(Step.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("id"), id)))
                : dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_step", allEntries = true)
    public Step save(Step entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Step entity) {
        if (entity == null) return;

        // Validate ManyToOne relationships exist
        if (entity.getStage() != null) {
            Stage stageEntity = entity.getStage();
            if (stageEntity.getId() != null) {
                Stage existingStage = stageService.findById(stageEntity.getId()).orElse(null);
                if (existingStage == null) {
                    throw new IllegalArgumentException("Stage with id " + stageEntity.getId() + " does not exist");
                }
                entity.setStage(existingStage);
            } else if (stageEntity.getRef() != null) {
                Stage existingStage = stageService.findByRef(stageEntity.getRef());
                if (existingStage == null) {
                    throw new IllegalArgumentException("Stage with ref '" + stageEntity.getRef() + "' does not exist");
                }
                entity.setStage(existingStage);
            } else {
                throw new IllegalArgumentException("Stage must be referenced by id or ref");
            }
        }
        // Handle OneToMany relationships - set parent reference
        // NOTE: checked for non-empty, not just non-null — the field is always non-null (empty
        // collection literal in the entity), so a plain != null check here misfires on every
        // request that never touches this relationship (e.g. converters that only set scalar
        // fields), wiping/reattaching a collection nothing actually asked to change.
        if (entity.getUserprogresses() != null && !entity.getUserprogresses().isEmpty()) {
            entity.getUserprogresses().forEach(child -> {
                if (child != null) {
                    child.setStep(entity);
                }
            });
        }
    }

    private void validateDeletionAllowed(Step entity) {
        if (entity == null) return;

        // Keep as an extension point for domain-specific delete guards.
    }

    private void prepareForDeletion(Step entity) {
        if (entity == null) return;

        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_step", allEntries = true)
    @Permit("Step:DELETE")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Step", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_step", allEntries = true)
    @Permit("Step:DELETE")
    public Optional<Step> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Step", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_step", allEntries = true)
    @Permit("Step:CREATE")
    public Step create(Step t) {
        Step result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Step", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_step", allEntries = true)
    @Permit("Step:UPDATE")
    public Step update(Step t) {
        if (t == null || t.getId() == null) return null;
        Step existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Step result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Step", result));
        }
        return result;
    }

    private void mergeEntityData(Step existing, Step updated) {
        if (existing == null || updated == null) return;

        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle().isEmpty() ? null : updated.getTitle());
        }
        if (updated.getContent() != null) {
            existing.setContent(updated.getContent().isEmpty() ? null : updated.getContent());
        }
        if (updated.getOrder() != null) {
            existing.setOrder(updated.getOrder());
        }
        if (updated.getEstimatedMinutes() != null) {
            existing.setEstimatedMinutes(updated.getEstimatedMinutes());
        }

        // Handle relationships
        if (updated.getStage() != null) {
            existing.setStage(updated.getStage());
        }
        // NOTE: checked for non-empty, not just non-null — this field is always non-null
        // (initialized to an empty collection on the entity), so a plain != null check fires on
        // every update() call, even DTOs/converters that never populate this relationship at
        // all. That unconditionally clears the real, persisted collection (deleting rows via
        // orphanRemoval) and, worse, can hand Hibernate a still-transient child with no cascade
        // configured on the association, throwing TransientObjectException on flush.
        if (updated.getUserprogresses() != null && !updated.getUserprogresses().isEmpty()) {
            if (existing.getUserprogresses() == null) {
                existing.setUserprogresses(new LinkedHashSet<>());
            } else {
                existing.getUserprogresses().clear();
            }

            updated.getUserprogresses().forEach(child -> {
                if (child != null) {
                    child.setStep(existing);
                    existing.getUserprogresses().add(child);
                }
            });
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Step> update(List<Step> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Step> result = new ArrayList<>();
        for (Step entity : ts) {
            if (entity.getId() != null) {
                Step updated = update(entity);
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
    public Step findOrSave(Step t) { 
        if (t == null) return null;
        
        Step existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Step findByReferenceEntity(Step t) { 
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
    public Step findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Step> findByCriteria(StepCriteria criteria) {
        Specification<Step> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new StepSpecification(criteria),
                rowFilter.forList(Step.class)
        );
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Step> findPaginatedByCriteria(StepCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Step> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new StepSpecification(criteria),
                rowFilter.forList(Step.class)
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(StepCriteria criteria) {
        Specification<Step> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new StepSpecification(criteria),
                rowFilter.forList(Step.class)
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Step> delete(List<Step> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Step> deleted = new ArrayList<>();
        for (Step entity : ts) {
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
    @Permit("Step:READ")
    public Step findByRef(String ref) {
        if (ref == null || ref.trim().isEmpty()) return null;
        Specification<Step> spec = rowFilter.forList(Step.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("ref"), ref))).orElse(null)
                : dao.findByRef(ref);
    }

    /**
     * Combines the caller's criteria Specification with the PolicyEngine row filter
     * (AND logic, null-tolerant on both sides).
     */
    private Specification<Step> combineSpecs(Specification<Step> criteriaSpec,
                                                       Specification<Step> authzSpec) {
        if (criteriaSpec == null && authzSpec == null) return null;
        if (criteriaSpec == null) return authzSpec;
        if (authzSpec == null) return criteriaSpec;
        return criteriaSpec.and(authzSpec);
    }
}

