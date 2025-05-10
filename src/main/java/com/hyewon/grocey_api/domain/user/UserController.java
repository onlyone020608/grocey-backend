package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.GenderUpdateRequest;
import com.hyewon.grocey_api.domain.user.dto.UserDetailDto;
import com.hyewon.grocey_api.domain.user.dto.UserSummaryDto;
import com.hyewon.grocey_api.domain.user.dto.UserUpdateRequest;
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

}
