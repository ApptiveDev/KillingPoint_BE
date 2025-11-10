package apptive.team5.global;

import apptive.team5.global.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<Map<String,String>>>>  handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream().map(error -> Map.of(error.getField(), Optional.ofNullable(error.getDefaultMessage()).orElse("Invalid value")))
                .toList();

        List<Map<String, String>> globalErrors = ex.getBindingResult().getGlobalErrors()
                .stream().map(error -> Map.of("message", Optional.ofNullable(error.getDefaultMessage()).orElse("Invalid value")))
                .toList();


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("globalErrors", globalErrors, "fieldErrors", fieldErrors));
    }

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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateException(DuplicateException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
    }
}
