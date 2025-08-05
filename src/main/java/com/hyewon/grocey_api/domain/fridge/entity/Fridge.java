package com.hyewon.grocey_api.domain.fridge.entity;

import com.hyewon.grocey_api.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Fridge {
    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "fridge")
    private List<User> users = new ArrayList<>();

    private Double fridgeTemperature;
    private Double freezerTemperature;

    public Fridge(Double fridgeTemperature, Double freezerTemperature) {
        this.fridgeTemperature = fridgeTemperature;
        this.freezerTemperature = freezerTemperature;
    }
}
