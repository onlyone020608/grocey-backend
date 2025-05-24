package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.*;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me/summary")
    public UserSummaryDto getSummary(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUserSummary(userDetails.getId());
    }

    @GetMapping("/me")
    public UserDetailDto getUserDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUserDetail(userDetails.getId());
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserUpdateRequest request) {
        userService.updateUser(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/me/gender")
    public ResponseEntity<Void> updateGender(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody GenderUpdateRequest request) {
        userService.updateGender(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/age-group")
    public ResponseEntity<Void> updateAgeGroup(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody AgeGroupUpdateRequest request) {
        userService.updateAgeGroup(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/allergies")
    public ResponseEntity<Void> updateUserAllergies(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserAllergyUpdateRequest request) {
        userService.updateUserAllergies(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/preferences")
    public ResponseEntity<Void> updatePreferences(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PreferenceUpdateRequest request) {
        userService.updateUserPreferences(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/vegan")
    public ResponseEntity<Void> updateVegan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody VeganUpdateRequest request) {
        userService.updateVeganStatus(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/status")
    public ResponseEntity<UserStatusDto> getUserStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean isCompleted = userService.checkProfileCompletion(userDetails.getId());
        return ResponseEntity.ok(new UserStatusDto(isCompleted));
    }







}
