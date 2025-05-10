package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT authentication
    @GetMapping("/{userId}/summary")
    public UserSummaryDto getSummary(@PathVariable Long userId) {
        return userService.getUserSummary(userId);
    }

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT authentication
    @GetMapping("/{userId}")
    public UserDetailDto getUserDetail(@PathVariable Long userId) {
        return userService.getUserDetail(userId);
    }

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT
    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUserInfo(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        userService.updateUser(userId, request);
        return ResponseEntity.ok().build();
    }

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT
    @PatchMapping("/{userId}/gender")
    public ResponseEntity<Void> updateGender(@PathVariable Long userId, @RequestBody GenderUpdateRequest request) {
        userService.updateGender(userId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/age-group")
    public ResponseEntity<Void> updateAgeGroup(@PathVariable Long userId, @RequestBody AgeGroupUpdateRequest request) {
        userService.updateAgeGroup(userId, request);
        return ResponseEntity.ok().build();
    }

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT
    @PatchMapping("/{userId}/allergies")
    public ResponseEntity<Void> updateUserAllergies(
            @PathVariable Long userId,
            @RequestBody UserAllergyUpdateRequest request) {
        userService.updateUserAllergies(userId, request);
        return ResponseEntity.ok().build();
    }
    // TODO: Replace with @AuthenticationPrincipal after implementing JWT
    @PatchMapping("/{userId}/preferences")
    public ResponseEntity<Void> updatePreferences(@PathVariable Long userId, @RequestBody PreferenceUpdateRequest request) {
        userService.updateUserPreferences(userId, request);
        return ResponseEntity.ok().build();
    }






}
