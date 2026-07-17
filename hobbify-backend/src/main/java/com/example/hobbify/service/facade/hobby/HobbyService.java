package com.example.hobbify.service.facade.hobby;

import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.dao.criteria.core.hobby.HobbyCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface HobbyService {

    Hobby create(Hobby t);
    Hobby update(Hobby t);
    List<Hobby> update(List<Hobby> ts, boolean createIfNotExist);
    Optional<Hobby> findById(Long id);
    Hobby save(Hobby entity);
    void deleteById(Long id);
    Optional<Hobby> findAndDeleteById(Long id);
    Hobby findOrSave(Hobby t);
    Hobby findByReferenceEntity(Hobby t);
    Hobby findWithAssociatedLists(Long id);
    List<Hobby> findAll();
    List<Hobby> findByCriteria(HobbyCriteria criteria);
    Page<Hobby> findPaginatedByCriteria(HobbyCriteria criteria, Pageable pageable);
    int getDataSize(HobbyCriteria criteria);
    List<Hobby> delete(List<Hobby> ts);
    Hobby findByRef(String ref);
}

