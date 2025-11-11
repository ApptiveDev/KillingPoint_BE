package apptive.team5.user.controller;

import apptive.team5.user.dto.UserResponse;
import apptive.team5.user.dto.UserTagUpdateRequest;
import apptive.team5.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/my")
    public ResponseEntity<Void> wirthDrawUser(@AuthenticationPrincipal Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/my/tags")
    public ResponseEntity<UserResponse> changeUserTag(@Valid @RequestBody UserTagUpdateRequest userTagUpdateRequest,
                                                @AuthenticationPrincipal Long userId) {

        UserResponse response = userService.changeTag(userTagUpdateRequest, userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUserList(@RequestParam(required = false) String tag,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Page<UserResponse> response = userService.findByTag(tag, PageRequest.of(page, size));


        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
