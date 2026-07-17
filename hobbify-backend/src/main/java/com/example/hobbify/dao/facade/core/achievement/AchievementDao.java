package com.example.hobbify.dao.facade.core.achievement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.example.hobbify.bean.core.achievement.Achievement;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementDao extends JpaRepository<Achievement, Long>, JpaSpecificationExecutor<Achievement> {

    Achievement findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"userachievements"})
    @Override
    Page<Achievement> findAll(@Nullable Specification<Achievement> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"userachievements"})
    @Query("SELECT e FROM Achievement e WHERE e.id = :id")
    Optional<Achievement> findWithAssociationsById(@Param("id") Long id);
}

