package com.example.hobbify.bean.core.hobby;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.LinkedHashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.example.hobbify.common.entity.BaseEntity;
import com.example.hobbify.bean.core.stage.Stage;
import com.example.hobbify.bean.core.favorite.Favorite;
import com.example.hobbify.bean.core.enums.CostTier;
import com.example.hobbify.bean.core.enums.SpaceNeeded;
import com.example.hobbify.bean.core.enums.TimeCommitment;
import com.example.hobbify.bean.core.enums.Difficulty;
import com.example.hobbify.bean.core.enums.ContentStatus;
import com.example.hobbify.bean.core.enums.ContentSource;

@Entity
@Table(name = "app_hobby")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Hobby extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String name;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(length = 500, nullable = false)
    private String category;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CostTier costTier;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceNeeded spaceNeeded;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeCommitment timeCommitment;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentSource contentSource;
    private LocalDateTime lastReviewedAt;

    @Column(name = "icon", length = 16)
    private String icon;
    @Column(name = "image_data", columnDefinition = "TEXT")
    private String imageData;

     @OneToMany(mappedBy = "hobby", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Stage> stages = new LinkedHashSet<>();
     @OneToMany(mappedBy = "hobby", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Favorite> favorites = new LinkedHashSet<>();

}

