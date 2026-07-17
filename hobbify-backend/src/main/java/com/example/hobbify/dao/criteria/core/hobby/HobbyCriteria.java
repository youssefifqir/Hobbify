package com.example.hobbify.dao.criteria.core.hobby;

import java.time.LocalDateTime;
import com.example.hobbify.bean.core.enums.CostTier;
import com.example.hobbify.bean.core.enums.SpaceNeeded;
import com.example.hobbify.bean.core.enums.TimeCommitment;
import com.example.hobbify.bean.core.enums.Difficulty;
import com.example.hobbify.bean.core.enums.ContentStatus;
import com.example.hobbify.bean.core.enums.ContentSource;

public class HobbyCriteria {

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
    private String category;
    private String categoryLike;
    private CostTier costTier;
    private SpaceNeeded spaceNeeded;
    private TimeCommitment timeCommitment;
    private Difficulty difficulty;
    private ContentStatus status;
    private ContentSource contentSource;
    private LocalDateTime lastReviewedAt;
    private LocalDateTime lastReviewedAtFrom;
    private LocalDateTime lastReviewedAtTo;


    // Constructors
    public HobbyCriteria() {
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

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCategoryLike() { return categoryLike; }
    public void setCategoryLike(String categoryLike) { this.categoryLike = categoryLike; }

    public CostTier getCostTier() { return costTier; }
    public void setCostTier(CostTier costTier) { this.costTier = costTier; }

    public SpaceNeeded getSpaceNeeded() { return spaceNeeded; }
    public void setSpaceNeeded(SpaceNeeded spaceNeeded) { this.spaceNeeded = spaceNeeded; }

    public TimeCommitment getTimeCommitment() { return timeCommitment; }
    public void setTimeCommitment(TimeCommitment timeCommitment) { this.timeCommitment = timeCommitment; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public ContentStatus getStatus() { return status; }
    public void setStatus(ContentStatus status) { this.status = status; }

    public ContentSource getContentSource() { return contentSource; }
    public void setContentSource(ContentSource contentSource) { this.contentSource = contentSource; }

    public LocalDateTime getLastReviewedAt() { return lastReviewedAt; }
    public void setLastReviewedAt(LocalDateTime lastReviewedAt) { this.lastReviewedAt = lastReviewedAt; }

    public LocalDateTime getLastReviewedAtFrom() { return lastReviewedAtFrom; }
    public void setLastReviewedAtFrom(LocalDateTime lastReviewedAtFrom) { this.lastReviewedAtFrom = lastReviewedAtFrom; }

    public LocalDateTime getLastReviewedAtTo() { return lastReviewedAtTo; }
    public void setLastReviewedAtTo(LocalDateTime lastReviewedAtTo) { this.lastReviewedAtTo = lastReviewedAtTo; }



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
        if (category != null && !category.trim().isEmpty()) return false;
        if (categoryLike != null && !categoryLike.trim().isEmpty()) return false;
        if (costTier != null) return false;
        if (spaceNeeded != null) return false;
        if (timeCommitment != null) return false;
        if (difficulty != null) return false;
        if (status != null) return false;
        if (contentSource != null) return false;
        if (lastReviewedAt != null) return false;
        if (lastReviewedAtFrom != null) return false;
        if (lastReviewedAtTo != null) return false;


        return true;
    }
}