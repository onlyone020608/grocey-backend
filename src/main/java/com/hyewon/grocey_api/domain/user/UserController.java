package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.UserDetailDto;
import com.hyewon.grocey_api.domain.user.dto.UserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
