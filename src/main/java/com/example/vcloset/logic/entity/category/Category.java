package com.example.vcloset.logic.entity.category;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.outfit.Outfit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "Category_clothing",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "clothing_id"))
    private Set<Clothing> clothing = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private Set<Outfit> outfits = new LinkedHashSet<>();

    public CategoryEnum getName() {
        return name;
    }

    public void setName(CategoryEnum name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Clothing> getClothing() {
        return clothing;
    }

    public void setClothing(Set<Clothing> clothing) {
        this.clothing = clothing;
    }

    public Set<Outfit> getOutfits() {
        return outfits;
    }

    public void setOutfits(Set<Outfit> outfits) {
        this.outfits = outfits;
    }
}
