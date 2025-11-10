package apptive.team5.user.controller;

import apptive.team5.user.dto.UserResponse;
import apptive.team5.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/my")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal Long userId) {

        UserResponse response = userService.getUserInfo(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
