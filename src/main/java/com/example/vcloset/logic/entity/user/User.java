package com.example.vcloset.logic.entity.user;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.loan.Loan;
import com.example.vcloset.logic.entity.outfit.Outfit;
import com.example.vcloset.logic.entity.recommendation.Recommendation;
import com.example.vcloset.logic.entity.rol.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
@Table(name = "user")
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String direction;
    private String dateOfBirth;
    private String picture;
    @Column(columnDefinition = "default=1")
    private boolean isUserActive = true;
    private boolean isPrivateProfile;


    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Recommendation> recommendations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "lenderUser", orphanRemoval = true)
    private Set<Loan> lends = new LinkedHashSet<>();

    @OneToMany(mappedBy = "lenderUser", orphanRemoval = true)
    private Set<Loan> loans = new LinkedHashSet<>();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
        return List.of(authority);
    }

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Clothing> clothing = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Outfit> outfits = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<com.example.vcloset.logic.entity.collection.Collection> collections = new LinkedHashSet<>();

    public Set<com.example.vcloset.logic.entity.collection.Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<com.example.vcloset.logic.entity.collection.Collection> collections) {
        this.collections = collections;
    }

    public Set<Outfit> getOutfits() {
        return outfits;
    }

    public void setOutfits(Set<Outfit> outfits) {
        this.outfits = outfits;
    }

    public Set<Clothing> getClothing() {
        return clothing;
    }

    public void setClothing(Set<Clothing> clothing) {
        this.clothing = clothing;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }

    public Set<Loan> getLends() {
        return lends;
    }

    public void setLends(Set<Loan> lends) {
        this.lends = lends;
    }

    public Set<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Set<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    // Constructors
    public User() {}


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;

        return this;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isIsUserActive() {
        return isUserActive;
    }

    public void setIsUserActive(boolean active) {
        this.isUserActive = active;
    }

    public boolean isIsProfileBlocked() {
        return isPrivateProfile;
    }

    public void setIsProfileBlocked(boolean blocked) {
        this.isPrivateProfile = blocked;
    }
}
