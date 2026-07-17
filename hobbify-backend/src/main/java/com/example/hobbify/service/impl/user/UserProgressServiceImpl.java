package com.example.hobbify.service.impl.user;

import com.example.hobbify.bean.core.user.UserProgress;
import com.example.hobbify.dao.criteria.core.user.UserProgressCriteria;
import com.example.hobbify.dao.facade.core.user.UserProgressDao;
import com.example.hobbify.dao.specification.core.user.UserProgressSpecification;
import com.example.hobbify.service.facade.user.UserProgressService;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.dao.facade.security.UserDao;
import com.example.hobbify.service.facade.step.StepService;
import com.example.hobbify.bean.core.step.Step;
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
public class UserProgressServiceImpl implements UserProgressService {

    private final UserProgressDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthorizationSpecificationAdvisor rowFilter;
    private final UserDao userDao;
    private final StepService stepService;

    public UserProgressServiceImpl(UserProgressDao dao, ApplicationEventPublisher eventPublisher, AuthorizationSpecificationAdvisor rowFilter, UserDao userDao, @Lazy StepService stepService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.rowFilter = rowFilter;
        this.userDao = userDao;
        this.stepService = stepService;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("UserProgress:READ")
    public List<UserProgress> findAll() {
        Specification<UserProgress> spec = rowFilter.forList(UserProgress.class);
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("UserProgress:READ")
    public Optional<UserProgress> findById(Long id) {
        Specification<UserProgress> spec = rowFilter.forList(UserProgress.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("id"), id)))
                : dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userprogress", allEntries = true)
    public UserProgress save(UserProgress entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(UserProgress entity) {
        if (entity == null) return;

        // Validate ManyToOne to User (security entity with String UUID id)
        if (entity.getUser() != null) {
            User userEntity = entity.getUser();
            if (userEntity.getId() != null) {
                User existingUser = this.userDao.findById(userEntity.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User with id " + userEntity.getId() + " does not exist"));
                entity.setUser(existingUser);
            } else {
                throw new IllegalArgumentException("User must be referenced by id");
            }
        }
        // Validate ManyToOne relationships exist
        if (entity.getStep() != null) {
            Step stepEntity = entity.getStep();
            if (stepEntity.getId() != null) {
                Step existingStep = stepService.findById(stepEntity.getId()).orElse(null);
                if (existingStep == null) {
                    throw new IllegalArgumentException("Step with id " + stepEntity.getId() + " does not exist");
                }
                entity.setStep(existingStep);
            } else if (stepEntity.getRef() != null) {
                Step existingStep = stepService.findByRef(stepEntity.getRef());
                if (existingStep == null) {
                    throw new IllegalArgumentException("Step with ref '" + stepEntity.getRef() + "' does not exist");
                }
                entity.setStep(existingStep);
            } else {
                throw new IllegalArgumentException("Step must be referenced by id or ref");
            }
        }
    }

    private void validateDeletionAllowed(UserProgress entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(UserProgress entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userprogress", allEntries = true)
    @Permit("UserProgress:DELETE")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("UserProgress", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userprogress", allEntries = true)
    @Permit("UserProgress:DELETE")
    public Optional<UserProgress> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("UserProgress", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userprogress", allEntries = true)
    @Permit("UserProgress:CREATE")
    public UserProgress create(UserProgress t) {
        UserProgress result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("UserProgress", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userprogress", allEntries = true)
    @Permit("UserProgress:UPDATE")
    public UserProgress update(UserProgress t) {
        if (t == null || t.getId() == null) return null;
        UserProgress existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        UserProgress result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("UserProgress", result));
        }
        return result;
    }

    private void mergeEntityData(UserProgress existing, UserProgress updated) {
        if (existing == null || updated == null) return;

        if (updated.getCompleted() != null) {
            existing.setCompleted(updated.getCompleted());
        }
        if (updated.getCompletedAt() != null) {
            existing.setCompletedAt(updated.getCompletedAt());
        }

        // Handle relationships
        if (updated.getUser() != null) {
            existing.setUser(updated.getUser());
        }
        if (updated.getStep() != null) {
            existing.setStep(updated.getStep());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<UserProgress> update(List<UserProgress> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<UserProgress> result = new ArrayList<>();
        for (UserProgress entity : ts) {
            if (entity.getId() != null) {
                UserProgress updated = update(entity);
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
    public UserProgress findOrSave(UserProgress t) { 
        if (t == null) return null;
        
        UserProgress existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProgress findByReferenceEntity(UserProgress t) { 
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
    public UserProgress findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProgress> findByCriteria(UserProgressCriteria criteria) {
        Specification<UserProgress> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new UserProgressSpecification(criteria),
                rowFilter.forList(UserProgress.class)
        );
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProgress> findPaginatedByCriteria(UserProgressCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<UserProgress> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new UserProgressSpecification(criteria),
                rowFilter.forList(UserProgress.class)
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(UserProgressCriteria criteria) {
        Specification<UserProgress> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new UserProgressSpecification(criteria),
                rowFilter.forList(UserProgress.class)
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<UserProgress> delete(List<UserProgress> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<UserProgress> deleted = new ArrayList<>();
        for (UserProgress entity : ts) {
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
    @Permit("UserProgress:READ")
    public UserProgress findByRef(String ref) {
        if (ref == null || ref.trim().isEmpty()) return null;
        Specification<UserProgress> spec = rowFilter.forList(UserProgress.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("ref"), ref))).orElse(null)
                : dao.findByRef(ref);
    }

    /**
     * Combines the caller's criteria Specification with the PolicyEngine row filter
     * (AND logic, null-tolerant on both sides).
     */
    private Specification<UserProgress> combineSpecs(Specification<UserProgress> criteriaSpec,
                                                       Specification<UserProgress> authzSpec) {
        if (criteriaSpec == null && authzSpec == null) return null;
        if (criteriaSpec == null) return authzSpec;
        if (authzSpec == null) return criteriaSpec;
        return criteriaSpec.and(authzSpec);
    }
}

