package apptive.team5.jwt.controller;

import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/exchange")
    public ResponseEntity<TokenResponse> tokenExchange(@RequestHeader(value = "X-Refresh-Token", required = false) String oldRefreshToken) {

        TokenResponse tokenResponse = jwtService.exchangeToken(oldRefreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }
}
