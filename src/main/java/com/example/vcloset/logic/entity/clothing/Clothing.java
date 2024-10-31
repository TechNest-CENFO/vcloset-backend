package com.example.vcloset.logic.entity.clothing;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.loan.Loan;
import com.example.vcloset.logic.entity.outfit.Outfit;
import com.example.vcloset.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Clothing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "clothing")
    private Set<Category> categories = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "clothing_type_id")
    private ClothingType clothingType;

    @ManyToMany
    @JoinTable(name = "clothing_outfit",
            joinColumns = @JoinColumn(name = "clothing_id"),
            inverseJoinColumns = @JoinColumn(name = "outfit_id"))
    private Set<Outfit> outfits = new LinkedHashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isPublic;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFavorite;

    @OneToMany(mappedBy = "clothing", orphanRemoval = true)
    private Set<Loan> loans = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String imageUrl;

}
