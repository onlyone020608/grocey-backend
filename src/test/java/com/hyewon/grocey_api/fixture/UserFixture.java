package com.hyewon.grocey_api.fixture;

import com.hyewon.grocey_api.domain.user.entity.AgeGroup;
import com.hyewon.grocey_api.domain.user.entity.Gender;
import com.hyewon.grocey_api.domain.user.entity.User;

public class UserFixture {

    public static User aDefaultUser() {
        return User.builder()
                .id(1L)
                .username("tester")
                .email("tester@email.com")
                .password("pw")
                .gender(Gender.FEMALE)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }
}
