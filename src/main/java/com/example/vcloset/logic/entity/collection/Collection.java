package com.example.vcloset.logic.entity.collection;

import com.example.vcloset.logic.entity.outfit.Outfit;
import com.example.vcloset.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "collections")
    private Set<Outfit> outfits = new LinkedHashSet<>();

}
