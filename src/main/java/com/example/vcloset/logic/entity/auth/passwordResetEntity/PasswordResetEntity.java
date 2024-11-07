package com.example.vcloset.logic.entity.auth.passwordResetEntity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PasswordResetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String token;

    private String email;

    private Date expirationDate;

    private String newPassword;

    public PasswordResetEntity() {
    }

    public PasswordResetEntity(Long id, String token, String email, Date expirationDate, String newPassword) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.expirationDate = expirationDate;
        this.newPassword = newPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNewPassword() { return newPassword; }

    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
