package com.hyewon.grocey_api.auth;

import com.hyewon.grocey_api.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private FridgeRepository fridgeRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("tester", "tester@email.com", "securepass");
    }

    @Test
    @DisplayName("signup - signup - successful registration")
    void signup_shouldSucceed() {
        // given
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(false);

        // when
        authService.signup(signupRequest);

        // then
        verify(fridgeRepository).save(any(Fridge.class));
        verify(userRepository).save(any(User.class));
    }
}