package apptive.team5.util;

import apptive.team5.user.domain.UserRoleType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

public final class TestSecurityContextHolderInjection {


    public static void inject(Long userId, UserRoleType roleType) {

        String role = "ROLE_" + roleType.name();

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }
}
