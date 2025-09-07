package apptive.team5.global;

import apptive.team5.global.exception.AuthenticationException;
import apptive.team5.global.exception.ExternalApiConnectException;
import apptive.team5.global.exception.KakaoApiConnectException;
import apptive.team5.global.exception.NotFoundEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String,String>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<Map<String,String>> handleNotFoundEntityException(NotFoundEntityException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String,String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getParameterName() + "는 필수값입니다."));
    }

    @ExceptionHandler(KakaoApiConnectException.class)
    public ResponseEntity<Map<String,Object>> handleKakaoApiConnectException(KakaoApiConnectException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(Map.of("message", e.getMessage(), "details", e.getKakaoApiExceptionResponse()));
    }

    @ExceptionHandler(ExternalApiConnectException.class)
    public ResponseEntity<Map<String,String>> handleExternalApiConnectException(ExternalApiConnectException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(Map.of("message", e.getMessage()));
    }
}
