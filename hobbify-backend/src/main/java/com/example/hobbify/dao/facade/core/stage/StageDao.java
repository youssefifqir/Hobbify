package com.example.hobbify.dao.facade.core.stage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.example.hobbify.bean.core.stage.Stage;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StageDao extends JpaRepository<Stage, Long>, JpaSpecificationExecutor<Stage> {

    Stage findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"hobby", "steps"})
    @Override
    Page<Stage> findAll(@Nullable Specification<Stage> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"hobby", "steps"})
    @Query("SELECT e FROM Stage e WHERE e.id = :id")
    Optional<Stage> findWithAssociationsById(@Param("id") Long id);
}

