package com.hyewon.grocey_api.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergy_id")
    private Allergy allergy;

    private UserAllergy(User user, Allergy allergy) {
        this.user = user;
        this.allergy = allergy;
    }

    public static UserAllergy of(User user,  Allergy allergy) {
        return new UserAllergy(user, allergy);
    }
}
