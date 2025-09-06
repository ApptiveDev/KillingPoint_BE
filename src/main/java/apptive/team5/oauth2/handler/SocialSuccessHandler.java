package apptive.team5.oauth2.handler;

import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class SocialSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String identifier = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();


        String accessToken = jwtUtil.createJWT(identifier, "ROLE_" + role, true);
        String refreshToken = jwtUtil.createJWT(identifier, "ROLE_"+role, false);

        jwtService.saveRefreshToken(identifier, refreshToken);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        String tokens = objectMapper.writeValueAsString(tokenResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(tokens);
    }
}
