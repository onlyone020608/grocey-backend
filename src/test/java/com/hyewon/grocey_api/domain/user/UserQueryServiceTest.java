package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.repository.UserRepository;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserQueryServiceTest {
    @InjectMocks
    UserQueryService userQueryService;

    @Mock
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .build();

    }

    @Test
    @DisplayName("getUserById - should return user")
    void getUserById_shouldSucceed() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(
                Optional.of(user));

        // when
        User resultUser = userQueryService.getUserById(userId);

        // then
        assertThat(resultUser).isEqualTo(user);
    }

    @Test
    @DisplayName("getUserById - should throw UserNotFoundException when user does not exist")
    void getUserById_shouldThrowException_whenUserNotFound() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(
                Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class,
                () ->  userQueryService.getUserById(userId));
    }

}
