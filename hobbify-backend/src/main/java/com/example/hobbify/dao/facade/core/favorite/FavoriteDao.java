package com.example.hobbify.dao.facade.core.favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.example.hobbify.bean.core.favorite.Favorite;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteDao extends JpaRepository<Favorite, Long>, JpaSpecificationExecutor<Favorite> {

    Favorite findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"user", "hobby"})
    @Override
    Page<Favorite> findAll(@Nullable Specification<Favorite> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "hobby"})
    @Query("SELECT e FROM Favorite e WHERE e.id = :id")
    Optional<Favorite> findWithAssociationsById(@Param("id") Long id);
}

