package com.example.hobbify.service.impl.user;

import com.example.hobbify.bean.core.user.UserAchievement;
import com.example.hobbify.dao.criteria.core.user.UserAchievementCriteria;
import com.example.hobbify.dao.facade.core.user.UserAchievementDao;
import com.example.hobbify.dao.specification.core.user.UserAchievementSpecification;
import com.example.hobbify.service.facade.user.UserAchievementService;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.dao.facade.security.UserDao;
import com.example.hobbify.service.facade.achievement.AchievementService;
import com.example.hobbify.bean.core.achievement.Achievement;
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
public class UserAchievementServiceImpl implements UserAchievementService {

    private final UserAchievementDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthorizationSpecificationAdvisor rowFilter;
    private final UserDao userDao;
    private final AchievementService achievementService;

    public UserAchievementServiceImpl(UserAchievementDao dao, ApplicationEventPublisher eventPublisher, AuthorizationSpecificationAdvisor rowFilter, UserDao userDao, @Lazy AchievementService achievementService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.rowFilter = rowFilter;
        this.userDao = userDao;
        this.achievementService = achievementService;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("UserAchievement:READ")
    public List<UserAchievement> findAll() {
        Specification<UserAchievement> spec = rowFilter.forList(UserAchievement.class);
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("UserAchievement:READ")
    public Optional<UserAchievement> findById(Long id) {
        Specification<UserAchievement> spec = rowFilter.forList(UserAchievement.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("id"), id)))
                : dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userachievement", allEntries = true)
    public UserAchievement save(UserAchievement entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(UserAchievement entity) {
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
        if (entity.getAchievement() != null) {
            Achievement achievementEntity = entity.getAchievement();
            if (achievementEntity.getId() != null) {
                Achievement existingAchievement = achievementService.findById(achievementEntity.getId()).orElse(null);
                if (existingAchievement == null) {
                    throw new IllegalArgumentException("Achievement with id " + achievementEntity.getId() + " does not exist");
                }
                entity.setAchievement(existingAchievement);
            } else if (achievementEntity.getRef() != null) {
                Achievement existingAchievement = achievementService.findByRef(achievementEntity.getRef());
                if (existingAchievement == null) {
                    throw new IllegalArgumentException("Achievement with ref '" + achievementEntity.getRef() + "' does not exist");
                }
                entity.setAchievement(existingAchievement);
            } else {
                throw new IllegalArgumentException("Achievement must be referenced by id or ref");
            }
        }
    }

    private void validateDeletionAllowed(UserAchievement entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(UserAchievement entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userachievement", allEntries = true)
    @Permit("UserAchievement:DELETE")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("UserAchievement", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userachievement", allEntries = true)
    @Permit("UserAchievement:DELETE")
    public Optional<UserAchievement> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("UserAchievement", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userachievement", allEntries = true)
    @Permit("UserAchievement:CREATE")
    public UserAchievement create(UserAchievement t) {
        UserAchievement result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("UserAchievement", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_userachievement", allEntries = true)
    @Permit("UserAchievement:UPDATE")
    public UserAchievement update(UserAchievement t) {
        if (t == null || t.getId() == null) return null;
        UserAchievement existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        UserAchievement result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("UserAchievement", result));
        }
        return result;
    }

    private void mergeEntityData(UserAchievement existing, UserAchievement updated) {
        if (existing == null || updated == null) return;

        if (updated.getEarnedAt() != null) {
            existing.setEarnedAt(updated.getEarnedAt());
        }

        // Handle relationships
        if (updated.getUser() != null) {
            existing.setUser(updated.getUser());
        }
        if (updated.getAchievement() != null) {
            existing.setAchievement(updated.getAchievement());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<UserAchievement> update(List<UserAchievement> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<UserAchievement> result = new ArrayList<>();
        for (UserAchievement entity : ts) {
            if (entity.getId() != null) {
                UserAchievement updated = update(entity);
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
    public UserAchievement findOrSave(UserAchievement t) { 
        if (t == null) return null;
        
        UserAchievement existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAchievement findByReferenceEntity(UserAchievement t) { 
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
    public UserAchievement findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievement> findByCriteria(UserAchievementCriteria criteria) {
        Specification<UserAchievement> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new UserAchievementSpecification(criteria),
                rowFilter.forList(UserAchievement.class)
        );
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAchievement> findPaginatedByCriteria(UserAchievementCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<UserAchievement> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new UserAchievementSpecification(criteria),
                rowFilter.forList(UserAchievement.class)
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(UserAchievementCriteria criteria) {
        Specification<UserAchievement> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new UserAchievementSpecification(criteria),
                rowFilter.forList(UserAchievement.class)
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<UserAchievement> delete(List<UserAchievement> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<UserAchievement> deleted = new ArrayList<>();
        for (UserAchievement entity : ts) {
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
    @Permit("UserAchievement:READ")
    public UserAchievement findByRef(String ref) {
        if (ref == null || ref.trim().isEmpty()) return null;
        Specification<UserAchievement> spec = rowFilter.forList(UserAchievement.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("ref"), ref))).orElse(null)
                : dao.findByRef(ref);
    }

    /**
     * Combines the caller's criteria Specification with the PolicyEngine row filter
     * (AND logic, null-tolerant on both sides).
     */
    private Specification<UserAchievement> combineSpecs(Specification<UserAchievement> criteriaSpec,
                                                       Specification<UserAchievement> authzSpec) {
        if (criteriaSpec == null && authzSpec == null) return null;
        if (criteriaSpec == null) return authzSpec;
        if (authzSpec == null) return criteriaSpec;
        return criteriaSpec.and(authzSpec);
    }
}

