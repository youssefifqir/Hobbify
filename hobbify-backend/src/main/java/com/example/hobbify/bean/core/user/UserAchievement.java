package com.example.hobbify.bean.core.user;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.example.hobbify.common.entity.BaseEntity;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.bean.core.achievement.Achievement;

@Entity
@Table(name = "app_userachievement")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class UserAchievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrePersist
    void stampAuthzDefaults() {
        if (this.user == null) {
            com.example.hobbify.config.security.authz.PrincipalContext.Snapshot principal =
                    com.example.hobbify.config.security.authz.PrincipalContext.currentOrAnonymous();
            if (!principal.isAnonymous()) {
                this.user = principal.user();
            }
        }
    }

    @Column(nullable = false)
    private LocalDateTime earnedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

}

