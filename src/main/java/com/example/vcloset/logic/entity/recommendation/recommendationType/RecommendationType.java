package com.example.vcloset.logic.entity.recommendation.recommendationType;

import com.example.vcloset.logic.entity.category.CategoryEnum;
import com.example.vcloset.logic.entity.recommendation.Recommendation;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class RecommendationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum name;


    @OneToMany(mappedBy = "recommendationType", orphanRemoval = true)
    private Set<Recommendation> recommendations = new LinkedHashSet<>();

}
