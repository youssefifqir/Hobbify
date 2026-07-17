package com.example.hobbify.dao.facade.core.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.example.hobbify.bean.core.user.UserProgress;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressDao extends JpaRepository<UserProgress, Long>, JpaSpecificationExecutor<UserProgress> {

    UserProgress findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"user", "step"})
    @Override
    Page<UserProgress> findAll(@Nullable Specification<UserProgress> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "step"})
    @Query("SELECT e FROM UserProgress e WHERE e.id = :id")
    Optional<UserProgress> findWithAssociationsById(@Param("id") Long id);
}

