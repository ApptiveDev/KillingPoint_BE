package apptive.team5.global.exception;

public class NotFoundEntityException extends RuntimeException {
  public NotFoundEntityException(ExceptionCode errorCode) {
    super(errorCode.getDescription());
  }
}
