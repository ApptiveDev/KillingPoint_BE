package apptive.team5.filter;

import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (!HttpMethod.POST.name().equals(method) || !requestURI.equals("/api/users/logout")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new ServletException("Invalid JWT Token");
        }

        String accessToken = authorization.split(" ")[1];


        Claims claims = jwtUtil.getClaims(accessToken);

        String identifier = claims.get("identifier").toString();

        jwtService.deleteRefreshTokenByIdentifier(identifier);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}