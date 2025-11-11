package apptive.team5.subscribe.controller;

import apptive.team5.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscribes")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;


    @PostMapping("/{subscribeToUserId}")
    public ResponseEntity<Void> subscribe(@PathVariable Long subscribeToUserId, @AuthenticationPrincipal Long userId) {

        subscribeService.save(subscribeToUserId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{subscribeToUserId}")
    public ResponseEntity<Void> cancelSubscribe(@PathVariable Long subscribeToUserId, @AuthenticationPrincipal Long userId) {

        subscribeService.deleteBySubscriberIdAndSubscribedToId(subscribeToUserId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
