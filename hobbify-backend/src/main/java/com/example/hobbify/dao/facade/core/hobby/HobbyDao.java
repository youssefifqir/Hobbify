package com.example.hobbify.dao.facade.core.hobby;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.example.hobbify.bean.core.hobby.Hobby;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HobbyDao extends JpaRepository<Hobby, Long>, JpaSpecificationExecutor<Hobby> {

    Hobby findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"stages", "favorites"})
    @Override
    Page<Hobby> findAll(@Nullable Specification<Hobby> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"stages", "favorites"})
    @Query("SELECT e FROM Hobby e WHERE e.id = :id")
    Optional<Hobby> findWithAssociationsById(@Param("id") Long id);
}

