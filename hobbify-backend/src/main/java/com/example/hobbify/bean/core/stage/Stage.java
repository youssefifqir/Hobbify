package com.example.hobbify.bean.core.stage;

import java.math.BigDecimal;
import java.util.Set;
import java.util.LinkedHashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.example.hobbify.common.entity.BaseEntity;
import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.bean.core.step.Step;

@Entity
@Table(name = "app_stage")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Stage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;
    @Column(name = "\"order\"", nullable = false)
    @Builder.Default
    private BigDecimal order = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hobby_id", nullable = false)
    private Hobby hobby;
     @OneToMany(mappedBy = "stage", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Step> steps = new LinkedHashSet<>();

}

