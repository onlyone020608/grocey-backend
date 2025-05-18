package com.hyewon.grocey_api.domain.user;


import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.recipe.SavedRecipe;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity {

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
    @JoinColumn(name = "fridge_id", nullable = true)
    private Fridge fridge;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedRecipe> savedRecipes = new ArrayList<>();

    public void assignFridge(Fridge fridge) {
        this.fridge = fridge;
        fridge.getUsers().add(this); // 양방향이라면 양쪽 연결
    }

    public User(String userName, String email, String password, AgeGroup ageGroup, Gender gender) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.ageGroup = ageGroup;
        this.gender = gender;
    }

    @Builder
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }



    public void updateName(String name) {
        this.userName = name;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }
    public void updateVeganStatus(boolean isVegan) {
        this.isVegan = isVegan;
    }


}
