package com.example.vcloset.logic.entity.season;

import com.example.vcloset.logic.entity.recommendation.Recommendation;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private SeasonEnum name;

    @OneToMany(mappedBy = "season", orphanRemoval = true)
    private Set<Recommendation> recommendations = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SeasonEnum getName() {
        return name;
    }

    public void setName(SeasonEnum name) {
        this.name = name;
    }

    public Set<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Set<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
