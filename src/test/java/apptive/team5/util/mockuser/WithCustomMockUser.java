package apptive.team5.util.mockuser;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(
        factory = WithMockCustomSecurityContextFactory.class
)
public @interface WithCustomMockUser {

    String identifier() default "GOOGLE-1234";
    String role() default "ROLE_USER";
}
