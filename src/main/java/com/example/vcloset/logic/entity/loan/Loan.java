package com.example.vcloset.logic.entity.loan;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Clothing clothing;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lender_user_id", nullable = false)
    private User lenderUser; //el que presta la prenda

    @ManyToOne(optional = false)
    @JoinColumn(name = "loaner_user_id", nullable = false)
    private User loanerUser; //el que solicita la prenda

    @Column(columnDefinition = "TINYINT(1) NOT NULL DEFAULT 5")
    private Integer lenderScore; //el puntaje que se va a otorgar al que presta la prenda

    @Column(columnDefinition = "TINYINT(1) NOT NULL DEFAULT 5")
    private Integer loanerScore; //el puntaje que se va a otorgar al que pide la prenda


    @Column(columnDefinition = "TINYINT(1)")
    private boolean isItemBorrowed;


    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isItemRequested;


    public Loan() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clothing getClothing() {
        return clothing;
    }

    public void setClothing(Clothing clothing) {
        this.clothing = clothing;
    }

    public User getLenderUser() {
        return lenderUser;
    }

    public void setLenderUser(User lenderUser) {
        this.lenderUser = lenderUser;
    }

    public User getLoanerUser() {
        return loanerUser;
    }

    public void setLoanerUser(User loanerUser) {
        this.loanerUser = loanerUser;
    }

    public Integer getLenderScore() {
        return lenderScore;
    }

    public void setLenderScore(Integer lenderScore) {
        this.lenderScore = lenderScore;
    }

    public Integer getLoanerScore() {
        return loanerScore;
    }

    public void setLoanerScore(Integer loanerScore) {
        this.loanerScore = loanerScore;
    }

    public Boolean getItemBorrowed() {
        return isItemBorrowed;
    }

    public void setItemBorrowed(Boolean itemBorrowed) {
        isItemBorrowed = itemBorrowed;
    }

    public Boolean getItemRequested() {
        return isItemRequested;
    }

    public void setItemRequested(Boolean itemRequested) {
        isItemRequested = itemRequested;
    }
}
