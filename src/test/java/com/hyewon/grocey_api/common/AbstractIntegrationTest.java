package com.hyewon.grocey_api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserAllergyRepository;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.security.JwtTokenProvider;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserRepository userRepository;

    @Autowired protected UserAllergyRepository userAllergyRepository;


    protected User createTestUser(String name, String email, String rawPassword) {
        User user = User.builder()
                .userName(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .build();
        return userRepository.save(user);
    }

    protected String generateTokenFor(User user) {
        return jwtTokenProvider.generateAccessToken(user.getId());
    }
}
