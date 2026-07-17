package com.example.hobbify.dao.criteria.core.user;

import java.time.LocalDateTime;

public class UserAchievementCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private LocalDateTime earnedAt;
    private LocalDateTime earnedAtFrom;
    private LocalDateTime earnedAtTo;

    private String userId;
    private Long achievementId;
    private String achievementRef;

    // Constructors
    public UserAchievementCriteria() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCreatedAtFrom() { return createdAtFrom; }
    public void setCreatedAtFrom(LocalDateTime createdAtFrom) { this.createdAtFrom = createdAtFrom; }

    public LocalDateTime getCreatedAtTo() { return createdAtTo; }
    public void setCreatedAtTo(LocalDateTime createdAtTo) { this.createdAtTo = createdAtTo; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getUpdatedAtFrom() { return updatedAtFrom; }
    public void setUpdatedAtFrom(LocalDateTime updatedAtFrom) { this.updatedAtFrom = updatedAtFrom; }

    public LocalDateTime getUpdatedAtTo() { return updatedAtTo; }
    public void setUpdatedAtTo(LocalDateTime updatedAtTo) { this.updatedAtTo = updatedAtTo; }

    public LocalDateTime getEarnedAt() { return earnedAt; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }

    public LocalDateTime getEarnedAtFrom() { return earnedAtFrom; }
    public void setEarnedAtFrom(LocalDateTime earnedAtFrom) { this.earnedAtFrom = earnedAtFrom; }

    public LocalDateTime getEarnedAtTo() { return earnedAtTo; }
    public void setEarnedAtTo(LocalDateTime earnedAtTo) { this.earnedAtTo = earnedAtTo; }


    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getAchievementId() { return achievementId; }
    public void setAchievementId(Long achievementId) { this.achievementId = achievementId; }

    public String getAchievementRef() { return achievementRef; }
    public void setAchievementRef(String achievementRef) { this.achievementRef = achievementRef; }


    // Utility method to check if criteria is empty
    public boolean isEmpty() {
        if (id != null) return false;
        if (ref != null && !ref.trim().isEmpty()) return false;
        if (createdAt != null) return false;
        if (createdAtFrom != null) return false;
        if (createdAtTo != null) return false;
        if (updatedAt != null) return false;
        if (updatedAtFrom != null) return false;
        if (updatedAtTo != null) return false;

        if (earnedAt != null) return false;
        if (earnedAtFrom != null) return false;
        if (earnedAtTo != null) return false;

        if (userId != null) return false;
        if (achievementId != null) return false;
        if (achievementRef != null && !achievementRef.trim().isEmpty()) return false;

        return true;
    }
}