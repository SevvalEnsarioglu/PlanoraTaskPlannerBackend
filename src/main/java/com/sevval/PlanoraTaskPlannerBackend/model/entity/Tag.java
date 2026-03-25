package com.sevval.PlanoraTaskPlannerBackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color_code")
    private String colorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();
}
