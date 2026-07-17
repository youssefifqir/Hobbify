package com.example.hobbify.dao.facade.core.step;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.example.hobbify.bean.core.step.Step;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StepDao extends JpaRepository<Step, Long>, JpaSpecificationExecutor<Step> {

    Step findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"stage", "userprogresses"})
    @Override
    Page<Step> findAll(@Nullable Specification<Step> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"stage", "userprogresses"})
    @Query("SELECT e FROM Step e WHERE e.id = :id")
    Optional<Step> findWithAssociationsById(@Param("id") Long id);
}

