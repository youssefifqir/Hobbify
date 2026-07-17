package com.example.hobbify.service.facade.user;

import com.example.hobbify.bean.core.user.UserProgress;
import com.example.hobbify.dao.criteria.core.user.UserProgressCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface UserProgressService {

    UserProgress create(UserProgress t);
    UserProgress update(UserProgress t);
    List<UserProgress> update(List<UserProgress> ts, boolean createIfNotExist);
    Optional<UserProgress> findById(Long id);
    UserProgress save(UserProgress entity);
    void deleteById(Long id);
    Optional<UserProgress> findAndDeleteById(Long id);
    UserProgress findOrSave(UserProgress t);
    UserProgress findByReferenceEntity(UserProgress t);
    UserProgress findWithAssociatedLists(Long id);
    List<UserProgress> findAll();
    List<UserProgress> findByCriteria(UserProgressCriteria criteria);
    Page<UserProgress> findPaginatedByCriteria(UserProgressCriteria criteria, Pageable pageable);
    int getDataSize(UserProgressCriteria criteria);
    List<UserProgress> delete(List<UserProgress> ts);
    UserProgress findByRef(String ref);
}

