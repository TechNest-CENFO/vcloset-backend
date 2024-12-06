package com.example.vcloset.logic.entity.clothing;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.loan.Loan;
import com.example.vcloset.logic.entity.outfit.Outfit;
import com.example.vcloset.logic.entity.user.User;
import com.fasterxml.jackson.annotation.*;
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

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "clothing_outfit",
            joinColumns = @JoinColumn(name = "clothing_id"),
            inverseJoinColumns = @JoinColumn(name = "outfit_id"))
    private Set<Outfit> outfits = new LinkedHashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isPublic;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFavorite;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isClothingItemActive;

    @JsonIgnore
    @OneToMany(mappedBy = "clothing", orphanRemoval = true)
    private Set<Loan> loans = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String imageUrl;

    private String season;

    private String color;

    public Clothing() {
        this.isFavorite = false;
        this.isPublic = true;
        this.isClothingItemActive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClothingType getClothingType() {
        return clothingType;
    }

    public void setClothingType(ClothingType clothingType) {
        this.clothingType = clothingType;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Outfit> getOutfits() {
        return outfits;
    }

    public void setOutfits(Set<Outfit> outfits) {
        this.outfits = outfits;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getClothingItemActive() {
        return isClothingItemActive;
    }

    public void setClothingItemActive(Boolean clothingItemActive) {
        isClothingItemActive = clothingItemActive;
    }
}
