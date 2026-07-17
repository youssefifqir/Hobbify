package com.example.hobbify.dao.criteria.core.step;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StepCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String title;
    private String titleLike;
    private String content;
    private String contentLike;
    private BigDecimal order;
    private BigDecimal orderMin;
    private BigDecimal orderMax;
    private BigDecimal estimatedMinutes;
    private BigDecimal estimatedMinutesMin;
    private BigDecimal estimatedMinutesMax;

    private Long stageId;
    private String stageRef;

    // Constructors
    public StepCriteria() {
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

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTitleLike() { return titleLike; }
    public void setTitleLike(String titleLike) { this.titleLike = titleLike; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getContentLike() { return contentLike; }
    public void setContentLike(String contentLike) { this.contentLike = contentLike; }

    public BigDecimal getOrder() { return order; }
    public void setOrder(BigDecimal order) { this.order = order; }

    public BigDecimal getOrderMin() { return orderMin; }
    public void setOrderMin(BigDecimal orderMin) { this.orderMin = orderMin; }

    public BigDecimal getOrderMax() { return orderMax; }
    public void setOrderMax(BigDecimal orderMax) { this.orderMax = orderMax; }

    public BigDecimal getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(BigDecimal estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }

    public BigDecimal getEstimatedMinutesMin() { return estimatedMinutesMin; }
    public void setEstimatedMinutesMin(BigDecimal estimatedMinutesMin) { this.estimatedMinutesMin = estimatedMinutesMin; }

    public BigDecimal getEstimatedMinutesMax() { return estimatedMinutesMax; }
    public void setEstimatedMinutesMax(BigDecimal estimatedMinutesMax) { this.estimatedMinutesMax = estimatedMinutesMax; }


    public Long getStageId() { return stageId; }
    public void setStageId(Long stageId) { this.stageId = stageId; }

    public String getStageRef() { return stageRef; }
    public void setStageRef(String stageRef) { this.stageRef = stageRef; }


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

        if (title != null && !title.trim().isEmpty()) return false;
        if (titleLike != null && !titleLike.trim().isEmpty()) return false;
        if (content != null && !content.trim().isEmpty()) return false;
        if (contentLike != null && !contentLike.trim().isEmpty()) return false;
        if (order != null) return false;
        if (orderMin != null) return false;
        if (orderMax != null) return false;
        if (estimatedMinutes != null) return false;
        if (estimatedMinutesMin != null) return false;
        if (estimatedMinutesMax != null) return false;

        if (stageId != null) return false;
        if (stageRef != null && !stageRef.trim().isEmpty()) return false;

        return true;
    }
}