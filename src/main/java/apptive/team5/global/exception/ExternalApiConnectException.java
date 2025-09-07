package apptive.team5.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExternalApiConnectException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ExternalApiConnectException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
