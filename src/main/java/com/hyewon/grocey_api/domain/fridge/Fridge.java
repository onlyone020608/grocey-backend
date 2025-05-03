package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Fridge {
    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "fridge")
    private List<User> users = new ArrayList<>();

    private Double fridgeTemperature;
    private Double freezerTemperature;

}
