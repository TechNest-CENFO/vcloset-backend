package com.example.vcloset.logic.entity.recommendation;

import com.example.vcloset.logic.entity.recommendation.recommendationType.RecommendationType;
import com.example.vcloset.logic.entity.season.Season;
import com.example.vcloset.logic.entity.user.User;
import jakarta.persistence.*;

@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recommendation_type_id", nullable = false)
    private RecommendationType recommendationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
