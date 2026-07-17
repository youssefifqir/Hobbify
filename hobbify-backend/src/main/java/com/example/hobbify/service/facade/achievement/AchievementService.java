package com.example.hobbify.service.facade.achievement;

import com.example.hobbify.bean.core.achievement.Achievement;
import com.example.hobbify.dao.criteria.core.achievement.AchievementCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface AchievementService {

    Achievement create(Achievement t);
    Achievement update(Achievement t);
    List<Achievement> update(List<Achievement> ts, boolean createIfNotExist);
    Optional<Achievement> findById(Long id);
    Achievement save(Achievement entity);
    void deleteById(Long id);
    Optional<Achievement> findAndDeleteById(Long id);
    Achievement findOrSave(Achievement t);
    Achievement findByReferenceEntity(Achievement t);
    Achievement findWithAssociatedLists(Long id);
    List<Achievement> findAll();
    List<Achievement> findByCriteria(AchievementCriteria criteria);
    Page<Achievement> findPaginatedByCriteria(AchievementCriteria criteria, Pageable pageable);
    int getDataSize(AchievementCriteria criteria);
    List<Achievement> delete(List<Achievement> ts);
    Achievement findByRef(String ref);
}

