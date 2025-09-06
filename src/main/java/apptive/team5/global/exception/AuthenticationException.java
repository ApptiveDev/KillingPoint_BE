package apptive.team5.global.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(ExceptionCode code) {
        super(code.getDescription());
    }
}
