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
import com.example.hobbify.bean.core.user.UserAchievement;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementDao extends JpaRepository<UserAchievement, Long>, JpaSpecificationExecutor<UserAchievement> {

    UserAchievement findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"achievement"})
    List<UserAchievement> findByUser_Id(String userId);

    @EntityGraph(attributePaths = {"user", "achievement"})
    @Override
    Page<UserAchievement> findAll(@Nullable Specification<UserAchievement> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "achievement"})
    @Query("SELECT e FROM UserAchievement e WHERE e.id = :id")
    Optional<UserAchievement> findWithAssociationsById(@Param("id") Long id);
}

