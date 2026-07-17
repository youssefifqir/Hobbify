package com.example.hobbify.dao.criteria.core.stage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StageCriteria {

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
    private BigDecimal order;
    private BigDecimal orderMin;
    private BigDecimal orderMax;

    private Long hobbyId;
    private String hobbyRef;

    // Constructors
    public StageCriteria() {
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

    public BigDecimal getOrder() { return order; }
    public void setOrder(BigDecimal order) { this.order = order; }

    public BigDecimal getOrderMin() { return orderMin; }
    public void setOrderMin(BigDecimal orderMin) { this.orderMin = orderMin; }

    public BigDecimal getOrderMax() { return orderMax; }
    public void setOrderMax(BigDecimal orderMax) { this.orderMax = orderMax; }


    public Long getHobbyId() { return hobbyId; }
    public void setHobbyId(Long hobbyId) { this.hobbyId = hobbyId; }

    public String getHobbyRef() { return hobbyRef; }
    public void setHobbyRef(String hobbyRef) { this.hobbyRef = hobbyRef; }


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
        if (order != null) return false;
        if (orderMin != null) return false;
        if (orderMax != null) return false;

        if (hobbyId != null) return false;
        if (hobbyRef != null && !hobbyRef.trim().isEmpty()) return false;

        return true;
    }
}