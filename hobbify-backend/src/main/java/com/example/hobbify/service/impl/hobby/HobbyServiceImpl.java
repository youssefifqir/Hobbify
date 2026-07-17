package com.example.hobbify.service.impl.hobby;

import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.dao.criteria.core.hobby.HobbyCriteria;
import com.example.hobbify.dao.facade.core.hobby.HobbyDao;
import com.example.hobbify.dao.specification.core.hobby.HobbySpecification;
import com.example.hobbify.service.facade.hobby.HobbyService;
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
public class HobbyServiceImpl implements HobbyService {

    private final HobbyDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthorizationSpecificationAdvisor rowFilter;

    public HobbyServiceImpl(HobbyDao dao, ApplicationEventPublisher eventPublisher, AuthorizationSpecificationAdvisor rowFilter) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.rowFilter = rowFilter;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Hobby:READ")
    public List<Hobby> findAll() {
        Specification<Hobby> spec = rowFilter.forList(Hobby.class);
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Hobby:READ")
    public Optional<Hobby> findById(Long id) {
        Specification<Hobby> spec = rowFilter.forList(Hobby.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("id"), id)))
                : dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_hobby", allEntries = true)
    public Hobby save(Hobby entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Hobby entity) {
        if (entity == null) return;

        // Handle OneToMany relationships - set parent reference
        // NOTE: checked for non-empty, not just non-null — the field is always non-null (empty
        // collection literal in the entity), so a plain != null check here misfires on every
        // request that never touches this relationship (e.g. converters that only set scalar
        // fields), wiping/reattaching a collection nothing actually asked to change.
        if (entity.getStages() != null && !entity.getStages().isEmpty()) {
            entity.getStages().forEach(child -> {
                if (child != null) {
                    child.setHobby(entity);
                }
            });
        }
        // Handle OneToMany relationships - set parent reference
        // NOTE: checked for non-empty, not just non-null — the field is always non-null (empty
        // collection literal in the entity), so a plain != null check here misfires on every
        // request that never touches this relationship (e.g. converters that only set scalar
        // fields), wiping/reattaching a collection nothing actually asked to change.
        if (entity.getFavorites() != null && !entity.getFavorites().isEmpty()) {
            entity.getFavorites().forEach(child -> {
                if (child != null) {
                    child.setHobby(entity);
                }
            });
        }
    }

    private void validateDeletionAllowed(Hobby entity) {
        if (entity == null) return;

        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
    }

    private void prepareForDeletion(Hobby entity) {
        if (entity == null) return;

        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_hobby", allEntries = true)
    @Permit("Hobby:DELETE")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Hobby", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_hobby", allEntries = true)
    @Permit("Hobby:DELETE")
    public Optional<Hobby> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Hobby", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_hobby", allEntries = true)
    @Permit("Hobby:CREATE")
    public Hobby create(Hobby t) {
        Hobby result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Hobby", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_hobby", allEntries = true)
    @Permit("Hobby:UPDATE")
    public Hobby update(Hobby t) {
        if (t == null || t.getId() == null) return null;
        Hobby existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Hobby result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Hobby", result));
        }
        return result;
    }

    private void mergeEntityData(Hobby existing, Hobby updated) {
        if (existing == null || updated == null) return;

        if (updated.getName() != null) {
            existing.setName(updated.getName().isEmpty() ? null : updated.getName());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().isEmpty() ? null : updated.getDescription());
        }
        if (updated.getCategory() != null) {
            existing.setCategory(updated.getCategory().isEmpty() ? null : updated.getCategory());
        }
        if (updated.getCostTier() != null) {
            existing.setCostTier(updated.getCostTier());
        }
        if (updated.getSpaceNeeded() != null) {
            existing.setSpaceNeeded(updated.getSpaceNeeded());
        }
        if (updated.getTimeCommitment() != null) {
            existing.setTimeCommitment(updated.getTimeCommitment());
        }
        if (updated.getDifficulty() != null) {
            existing.setDifficulty(updated.getDifficulty());
        }
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getContentSource() != null) {
            existing.setContentSource(updated.getContentSource());
        }
        if (updated.getLastReviewedAt() != null) {
            existing.setLastReviewedAt(updated.getLastReviewedAt());
        }
        if (updated.getIcon() != null) {
            existing.setIcon(updated.getIcon().isEmpty() ? null : updated.getIcon());
        }
        if (updated.getImageData() != null) {
            existing.setImageData(updated.getImageData().isEmpty() ? null : updated.getImageData());
        }

        // Handle relationships
        // NOTE: checked for non-empty, not just non-null — this field is always non-null
        // (initialized to an empty collection on the entity), so a plain != null check fires on
        // every update() call, even DTOs/converters that never populate this relationship at
        // all. That unconditionally clears the real, persisted collection (deleting rows via
        // orphanRemoval) and, worse, can hand Hibernate a still-transient child with no cascade
        // configured on the association, throwing TransientObjectException on flush.
        if (updated.getStages() != null && !updated.getStages().isEmpty()) {
            if (existing.getStages() == null) {
                existing.setStages(new LinkedHashSet<>());
            } else {
                existing.getStages().clear();
            }

            updated.getStages().forEach(child -> {
                if (child != null) {
                    child.setHobby(existing);
                    existing.getStages().add(child);
                }
            });
        }
        // NOTE: checked for non-empty, not just non-null — this field is always non-null
        // (initialized to an empty collection on the entity), so a plain != null check fires on
        // every update() call, even DTOs/converters that never populate this relationship at
        // all. That unconditionally clears the real, persisted collection (deleting rows via
        // orphanRemoval) and, worse, can hand Hibernate a still-transient child with no cascade
        // configured on the association, throwing TransientObjectException on flush.
        if (updated.getFavorites() != null && !updated.getFavorites().isEmpty()) {
            if (existing.getFavorites() == null) {
                existing.setFavorites(new LinkedHashSet<>());
            } else {
                existing.getFavorites().clear();
            }

            updated.getFavorites().forEach(child -> {
                if (child != null) {
                    child.setHobby(existing);
                    existing.getFavorites().add(child);
                }
            });
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Hobby> update(List<Hobby> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Hobby> result = new ArrayList<>();
        for (Hobby entity : ts) {
            if (entity.getId() != null) {
                Hobby updated = update(entity);
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
    public Hobby findOrSave(Hobby t) { 
        if (t == null) return null;
        
        Hobby existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Hobby findByReferenceEntity(Hobby t) { 
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
    public Hobby findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hobby> findByCriteria(HobbyCriteria criteria) {
        Specification<Hobby> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new HobbySpecification(criteria),
                rowFilter.forList(Hobby.class)
        );
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Hobby> findPaginatedByCriteria(HobbyCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Hobby> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new HobbySpecification(criteria),
                rowFilter.forList(Hobby.class)
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(HobbyCriteria criteria) {
        Specification<Hobby> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new HobbySpecification(criteria),
                rowFilter.forList(Hobby.class)
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Hobby> delete(List<Hobby> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Hobby> deleted = new ArrayList<>();
        for (Hobby entity : ts) {
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
    @Permit("Hobby:READ")
    public Hobby findByRef(String ref) {
        if (ref == null || ref.trim().isEmpty()) return null;
        Specification<Hobby> spec = rowFilter.forList(Hobby.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("ref"), ref))).orElse(null)
                : dao.findByRef(ref);
    }

    /**
     * Combines the caller's criteria Specification with the PolicyEngine row filter
     * (AND logic, null-tolerant on both sides).
     */
    private Specification<Hobby> combineSpecs(Specification<Hobby> criteriaSpec,
                                                       Specification<Hobby> authzSpec) {
        if (criteriaSpec == null && authzSpec == null) return null;
        if (criteriaSpec == null) return authzSpec;
        if (authzSpec == null) return criteriaSpec;
        return criteriaSpec.and(authzSpec);
    }
}

