package com.example.hobbify.bean.core.step;

import java.math.BigDecimal;
import java.util.Set;
import java.util.LinkedHashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.example.hobbify.common.entity.BaseEntity;
import com.example.hobbify.bean.core.stage.Stage;
import com.example.hobbify.bean.core.user.UserProgress;

@Entity
@Table(name = "app_step")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Step extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(name = "\"order\"", nullable = false)
    @Builder.Default
    private BigDecimal order = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal estimatedMinutes = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;
     @OneToMany(mappedBy = "step", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<UserProgress> userprogresses = new LinkedHashSet<>();

}

