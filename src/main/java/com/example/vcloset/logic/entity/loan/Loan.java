package com.example.vcloset.logic.entity.loan;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.user.User;
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


}
