package com.example.vcloset.logic.entity.outfit;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.collection.Collection;
import com.example.vcloset.logic.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Outfit_collections",
            joinColumns = @JoinColumn(name = "outfit_"),
            inverseJoinColumns = @JoinColumn(name = "collections_id"))
    private Set<Collection> collections = new LinkedHashSet<>();

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isDeleted = false;

    public Outfit() {
    }

    public Outfit(Integer id, String name, Set<Clothing> clothing, Boolean isPublic, Boolean isFavorite, Category category, User user, Set<Collection> collections, String imageUrl, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.clothing = clothing;
        this.isPublic = isPublic;
        this.isFavorite = isFavorite;
        this.category = category;
        this.user = user;
        this.collections = collections;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Clothing> getClothing() {
        return clothing;
    }

    public void setClothing(Set<Clothing> clothing) {
        this.clothing = clothing;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
