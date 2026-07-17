package com.example.hobbify.service.facade.stage;

import com.example.hobbify.bean.core.stage.Stage;
import com.example.hobbify.dao.criteria.core.stage.StageCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface StageService {

    Stage create(Stage t);
    Stage update(Stage t);
    List<Stage> update(List<Stage> ts, boolean createIfNotExist);
    Optional<Stage> findById(Long id);
    Stage save(Stage entity);
    void deleteById(Long id);
    Optional<Stage> findAndDeleteById(Long id);
    Stage findOrSave(Stage t);
    Stage findByReferenceEntity(Stage t);
    Stage findWithAssociatedLists(Long id);
    List<Stage> findAll();
    List<Stage> findByCriteria(StageCriteria criteria);
    Page<Stage> findPaginatedByCriteria(StageCriteria criteria, Pageable pageable);
    int getDataSize(StageCriteria criteria);
    List<Stage> delete(List<Stage> ts);
    Stage findByRef(String ref);
}

