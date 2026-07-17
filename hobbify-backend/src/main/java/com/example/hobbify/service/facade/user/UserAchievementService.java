package com.example.hobbify.service.facade.user;

import com.example.hobbify.bean.core.user.UserAchievement;
import com.example.hobbify.dao.criteria.core.user.UserAchievementCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface UserAchievementService {

    UserAchievement create(UserAchievement t);
    UserAchievement update(UserAchievement t);
    List<UserAchievement> update(List<UserAchievement> ts, boolean createIfNotExist);
    Optional<UserAchievement> findById(Long id);
    UserAchievement save(UserAchievement entity);
    void deleteById(Long id);
    Optional<UserAchievement> findAndDeleteById(Long id);
    UserAchievement findOrSave(UserAchievement t);
    UserAchievement findByReferenceEntity(UserAchievement t);
    UserAchievement findWithAssociatedLists(Long id);
    List<UserAchievement> findAll();
    List<UserAchievement> findByCriteria(UserAchievementCriteria criteria);
    Page<UserAchievement> findPaginatedByCriteria(UserAchievementCriteria criteria, Pageable pageable);
    int getDataSize(UserAchievementCriteria criteria);
    List<UserAchievement> delete(List<UserAchievement> ts);
    UserAchievement findByRef(String ref);
}

