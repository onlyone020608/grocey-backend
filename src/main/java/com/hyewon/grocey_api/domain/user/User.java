package com.hyewon.grocey_api.domain.user;


import com.hyewon.grocey_api.domain.fridge.Fridge;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    private Boolean isVegan = false;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;



}
