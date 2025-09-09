package com.hyewon.grocey_api.domain.user.repository;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    @Query("select u.fridge from User u where u.id = :userId")
    Optional<Fridge> findFridgeByUserId(Long userId);
}
