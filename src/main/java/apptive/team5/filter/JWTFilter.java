package apptive.team5.filter;

import apptive.team5.global.exception.NotFoundEntityException;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserLowService userLowService;

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

        if (!jwtUtil.validateToken(accessToken, true)) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("유효하지 않은 토큰입니다.");
            return;
        }

        Claims claims = jwtUtil.getClaims(accessToken);
        String identifier = claims.get("identifier").toString();

        try {
            UserEntity findUser = userLowService.findByIdentifier(identifier);
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+findUser.getRoleType().name()));
            Authentication auth = new UsernamePasswordAuthenticationToken(identifier, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (NotFoundEntityException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}