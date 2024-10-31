package com.example.vcloset.logic.entity.outfit;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.collection.Collection;
import com.example.vcloset.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "outfits")
    @Column(nullable = false)
    private Set<Clothing> clothing = new LinkedHashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isPublic;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFavorite;


    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "Outfit_collections",
            joinColumns = @JoinColumn(name = "outfit_"),
            inverseJoinColumns = @JoinColumn(name = "collections_id"))
    private Set<Collection> collections = new LinkedHashSet<>();

    @Column(nullable = false)
    private String imageUrl;

}
