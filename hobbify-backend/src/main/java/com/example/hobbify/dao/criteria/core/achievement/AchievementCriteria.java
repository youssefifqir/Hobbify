package com.example.hobbify.dao.criteria.core.achievement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.hobbify.bean.core.enums.AchievementType;

public class AchievementCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String name;
    private String nameLike;
    private String description;
    private String descriptionLike;
    private AchievementType type;
    private BigDecimal threshold;
    private BigDecimal thresholdMin;
    private BigDecimal thresholdMax;
    private String iconUrl;
    private String iconUrlLike;


    // Constructors
    public AchievementCriteria() {
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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameLike() { return nameLike; }
    public void setNameLike(String nameLike) { this.nameLike = nameLike; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionLike() { return descriptionLike; }
    public void setDescriptionLike(String descriptionLike) { this.descriptionLike = descriptionLike; }

    public AchievementType getType() { return type; }
    public void setType(AchievementType type) { this.type = type; }

    public BigDecimal getThreshold() { return threshold; }
    public void setThreshold(BigDecimal threshold) { this.threshold = threshold; }

    public BigDecimal getThresholdMin() { return thresholdMin; }
    public void setThresholdMin(BigDecimal thresholdMin) { this.thresholdMin = thresholdMin; }

    public BigDecimal getThresholdMax() { return thresholdMax; }
    public void setThresholdMax(BigDecimal thresholdMax) { this.thresholdMax = thresholdMax; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getIconUrlLike() { return iconUrlLike; }
    public void setIconUrlLike(String iconUrlLike) { this.iconUrlLike = iconUrlLike; }



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

        if (name != null && !name.trim().isEmpty()) return false;
        if (nameLike != null && !nameLike.trim().isEmpty()) return false;
        if (description != null && !description.trim().isEmpty()) return false;
        if (descriptionLike != null && !descriptionLike.trim().isEmpty()) return false;
        if (type != null) return false;
        if (threshold != null) return false;
        if (thresholdMin != null) return false;
        if (thresholdMax != null) return false;
        if (iconUrl != null && !iconUrl.trim().isEmpty()) return false;
        if (iconUrlLike != null && !iconUrlLike.trim().isEmpty()) return false;


        return true;
    }
}