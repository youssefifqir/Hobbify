package com.example.hobbify.dao.criteria.core.user;

import java.time.LocalDateTime;

public class UserProgressCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private Boolean completed;
    private LocalDateTime completedAt;
    private LocalDateTime completedAtFrom;
    private LocalDateTime completedAtTo;

    private String userId;
    private Long stepId;
    private String stepRef;

    // Constructors
    public UserProgressCriteria() {
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

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCompletedAtFrom() { return completedAtFrom; }
    public void setCompletedAtFrom(LocalDateTime completedAtFrom) { this.completedAtFrom = completedAtFrom; }

    public LocalDateTime getCompletedAtTo() { return completedAtTo; }
    public void setCompletedAtTo(LocalDateTime completedAtTo) { this.completedAtTo = completedAtTo; }


    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getStepId() { return stepId; }
    public void setStepId(Long stepId) { this.stepId = stepId; }

    public String getStepRef() { return stepRef; }
    public void setStepRef(String stepRef) { this.stepRef = stepRef; }


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

        if (completed != null) return false;
        if (completedAt != null) return false;
        if (completedAtFrom != null) return false;
        if (completedAtTo != null) return false;

        if (userId != null) return false;
        if (stepId != null) return false;
        if (stepRef != null && !stepRef.trim().isEmpty()) return false;

        return true;
    }
}