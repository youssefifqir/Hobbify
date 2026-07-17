package com.example.hobbify.bean.core.achievement;

import java.math.BigDecimal;
import java.util.Set;
import java.util.LinkedHashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.example.hobbify.common.entity.BaseEntity;
import com.example.hobbify.bean.core.user.UserAchievement;
import com.example.hobbify.bean.core.enums.AchievementType;

@Entity
@Table(name = "app_achievement")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Achievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String name;
    @Column(length = 500, nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementType type;
    @Column(nullable = false)
    @Builder.Default
    private BigDecimal threshold = BigDecimal.ZERO;
    @Column(length = 500)
    private String iconUrl;

     @OneToMany(mappedBy = "achievement", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<UserAchievement> userachievements = new LinkedHashSet<>();

}

