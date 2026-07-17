package com.example.hobbify.service.facade.favorite;

import com.example.hobbify.bean.core.favorite.Favorite;
import com.example.hobbify.dao.criteria.core.favorite.FavoriteCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface FavoriteService {

    Favorite create(Favorite t);
    Favorite update(Favorite t);
    List<Favorite> update(List<Favorite> ts, boolean createIfNotExist);
    Optional<Favorite> findById(Long id);
    Favorite save(Favorite entity);
    void deleteById(Long id);
    Optional<Favorite> findAndDeleteById(Long id);
    Favorite findOrSave(Favorite t);
    Favorite findByReferenceEntity(Favorite t);
    Favorite findWithAssociatedLists(Long id);
    List<Favorite> findAll();
    List<Favorite> findByCriteria(FavoriteCriteria criteria);
    Page<Favorite> findPaginatedByCriteria(FavoriteCriteria criteria, Pageable pageable);
    int getDataSize(FavoriteCriteria criteria);
    List<Favorite> delete(List<Favorite> ts);
    Favorite findByRef(String ref);
}

