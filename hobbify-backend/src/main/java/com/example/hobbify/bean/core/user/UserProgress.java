package com.example.hobbify.bean.core.user;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.example.hobbify.common.entity.BaseEntity;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.bean.core.step.Step;

@Entity
@Table(name = "app_userprogress")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class UserProgress extends BaseEntity {

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
    private Boolean completed;
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "step_id", nullable = false)
    private Step step;

}

