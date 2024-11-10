package com.example.vcloset.logic.entity.clothing.clothingType;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.STRING)
    private ClothingTypeEnum.SubType subType;

    @Enumerated(EnumType.STRING)
    private ClothingTypeEnum.SubType.Type type;

    @JsonIgnore
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

    public ClothingTypeEnum.SubType getSubType() {
        return subType;
    }

    public void setSubType(ClothingTypeEnum.SubType subType) {
        this.subType = subType;
    }

    public ClothingTypeEnum.SubType.Type getType() {
        return type;
    }

    public void setType(ClothingTypeEnum.SubType.Type type) {
        this.type = type;
    }

    public Set<Clothing> getClothing() {
        return clothing;
    }

    public void setClothing(Set<Clothing> clothing) {
        this.clothing = clothing;
    }
}