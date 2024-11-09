package com.example.vcloset.logic.entity.auth.passwordRecovery;

import jakarta.persistence.*;

@Entity
public class ToAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String toAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ToAddress() {
    }

    public ToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
}
