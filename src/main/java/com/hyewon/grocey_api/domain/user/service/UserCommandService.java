package com.hyewon.grocey_api.domain.user.service;

import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
