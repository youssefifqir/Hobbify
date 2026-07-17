package com.example.hobbify.service.facade.step;

import com.example.hobbify.bean.core.step.Step;
import com.example.hobbify.dao.criteria.core.step.StepCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface StepService {

    Step create(Step t);
    Step update(Step t);
    List<Step> update(List<Step> ts, boolean createIfNotExist);
    Optional<Step> findById(Long id);
    Step save(Step entity);
    void deleteById(Long id);
    Optional<Step> findAndDeleteById(Long id);
    Step findOrSave(Step t);
    Step findByReferenceEntity(Step t);
    Step findWithAssociatedLists(Long id);
    List<Step> findAll();
    List<Step> findByCriteria(StepCriteria criteria);
    Page<Step> findPaginatedByCriteria(StepCriteria criteria, Pageable pageable);
    int getDataSize(StepCriteria criteria);
    List<Step> delete(List<Step> ts);
    Step findByRef(String ref);
}

