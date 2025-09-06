package apptive.team5.config;

import apptive.team5.filter.CustomLogoutFilter;
import apptive.team5.filter.JWTFilter;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.service.JwtService;
import apptive.team5.oauth2.handler.SocialSuccessHandler;
import apptive.team5.oauth2.service.OAuth2UserService;
import apptive.team5.user.service.UserLowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final JwtService jwtService;
    private final OAuth2UserService oAuth2UserService;
    private final UserLowService userLowService;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth->auth
                .anyRequest().permitAll());

        http.oauth2Login(oauth2->oauth2
                .userInfoEndpoint((userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(oAuth2UserService)))
                .successHandler(new SocialSuccessHandler(jwtUtil, jwtService,objectMapper)))
        ;

        http.exceptionHandling(e->e
                .authenticationEntryPoint((request, response, authException)-> {
                    if (request.getRequestURI().startsWith("/admin")) response.sendRedirect("/login");
                    else response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .accessDeniedHandler((request, response, authException)-> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }));

        http.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JWTFilter(jwtUtil,userLowService), LogoutFilter.class);

        http.addFilterAt(new CustomLogoutFilter(jwtUtil, jwtService), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "X-Refresh-Token"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}