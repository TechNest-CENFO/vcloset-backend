package com.example.vcloset.logic.entity.clothing.clothingType;

import com.example.vcloset.logic.entity.clothing.Clothing;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class ClothingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private ClothingTypeEnum name;

    @OneToMany(mappedBy = "clothingType", orphanRemoval = true)
    private Set<Clothing> clothing = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClothingTypeEnum getName() {
        return name;
    }

    public void setName(ClothingTypeEnum name) {
        this.name = name;
    }

    public Set<Clothing> getClothing() {
        return clothing;
    }

    public void setClothing(Set<Clothing> clothing) {
        this.clothing = clothing;
    }
}
