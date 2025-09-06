package apptive.team5.global.exception;

public class ExternalApiConnectException extends RuntimeException {
    public ExternalApiConnectException(ExceptionCode exceptionCode) {
        super(exceptionCode.getDescription());
    }
}
