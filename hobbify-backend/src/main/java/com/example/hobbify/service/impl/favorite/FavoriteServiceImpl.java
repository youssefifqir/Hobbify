package com.example.hobbify.service.impl.favorite;

import com.example.hobbify.bean.core.favorite.Favorite;
import com.example.hobbify.dao.criteria.core.favorite.FavoriteCriteria;
import com.example.hobbify.dao.facade.core.favorite.FavoriteDao;
import com.example.hobbify.dao.specification.core.favorite.FavoriteSpecification;
import com.example.hobbify.service.facade.favorite.FavoriteService;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.dao.facade.security.UserDao;
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
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthorizationSpecificationAdvisor rowFilter;
    private final UserDao userDao;
    private final HobbyService hobbyService;

    public FavoriteServiceImpl(FavoriteDao dao, ApplicationEventPublisher eventPublisher, AuthorizationSpecificationAdvisor rowFilter, UserDao userDao, @Lazy HobbyService hobbyService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.rowFilter = rowFilter;
        this.userDao = userDao;
        this.hobbyService = hobbyService;
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Favorite:READ")
    public List<Favorite> findAll() {
        Specification<Favorite> spec = rowFilter.forList(Favorite.class);
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Permit("Favorite:READ")
    public Optional<Favorite> findById(Long id) {
        Specification<Favorite> spec = rowFilter.forList(Favorite.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("id"), id)))
                : dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_favorite", allEntries = true)
    public Favorite save(Favorite entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Favorite entity) {
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
    }

    private void validateDeletionAllowed(Favorite entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(Favorite entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_favorite", allEntries = true)
    @Permit("Favorite:DELETE")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Favorite", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_favorite", allEntries = true)
    @Permit("Favorite:DELETE")
    public Optional<Favorite> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Favorite", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_favorite", allEntries = true)
    @Permit("Favorite:CREATE")
    public Favorite create(Favorite t) {
        Favorite result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Favorite", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_favorite", allEntries = true)
    @Permit("Favorite:UPDATE")
    public Favorite update(Favorite t) {
        if (t == null || t.getId() == null) return null;
        Favorite existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Favorite result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Favorite", result));
        }
        return result;
    }

    private void mergeEntityData(Favorite existing, Favorite updated) {
        if (existing == null || updated == null) return;


        // Handle relationships
        if (updated.getUser() != null) {
            existing.setUser(updated.getUser());
        }
        if (updated.getHobby() != null) {
            existing.setHobby(updated.getHobby());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Favorite> update(List<Favorite> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Favorite> result = new ArrayList<>();
        for (Favorite entity : ts) {
            if (entity.getId() != null) {
                Favorite updated = update(entity);
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
    public Favorite findOrSave(Favorite t) { 
        if (t == null) return null;
        
        Favorite existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Favorite findByReferenceEntity(Favorite t) { 
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
    public Favorite findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Favorite> findByCriteria(FavoriteCriteria criteria) {
        Specification<Favorite> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new FavoriteSpecification(criteria),
                rowFilter.forList(Favorite.class)
        );
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Favorite> findPaginatedByCriteria(FavoriteCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Favorite> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new FavoriteSpecification(criteria),
                rowFilter.forList(Favorite.class)
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(FavoriteCriteria criteria) {
        Specification<Favorite> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new FavoriteSpecification(criteria),
                rowFilter.forList(Favorite.class)
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Favorite> delete(List<Favorite> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Favorite> deleted = new ArrayList<>();
        for (Favorite entity : ts) {
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
    @Permit("Favorite:READ")
    public Favorite findByRef(String ref) {
        if (ref == null || ref.trim().isEmpty()) return null;
        Specification<Favorite> spec = rowFilter.forList(Favorite.class);
        return spec != null
                ? dao.findOne(spec.and((root, query, cb) -> cb.equal(root.get("ref"), ref))).orElse(null)
                : dao.findByRef(ref);
    }

    /**
     * Combines the caller's criteria Specification with the PolicyEngine row filter
     * (AND logic, null-tolerant on both sides).
     */
    private Specification<Favorite> combineSpecs(Specification<Favorite> criteriaSpec,
                                                       Specification<Favorite> authzSpec) {
        if (criteriaSpec == null && authzSpec == null) return null;
        if (criteriaSpec == null) return authzSpec;
        if (authzSpec == null) return criteriaSpec;
        return criteriaSpec.and(authzSpec);
    }
}

