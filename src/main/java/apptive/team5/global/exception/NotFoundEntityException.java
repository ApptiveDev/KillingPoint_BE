package apptive.team5.global.exception;

public class NotFoundEntityException extends RuntimeException {
  public NotFoundEntityException(ErrorCode errorCode) {
    super(errorCode.getDescription());
  }
}
