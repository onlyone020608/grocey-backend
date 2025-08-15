package com.hyewon.grocey_api.domain.user.entity;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    private Boolean vegan = false;

    private Boolean profileCompleted = false;

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

    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static User of(String username, String email, String password){
        return new User(username, email, password);
    }

    public void updateName(String name) {
        this.username = name;
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
        this.vegan = isVegan;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void completeProfile() {
        this.profileCompleted = true;
    }
}
