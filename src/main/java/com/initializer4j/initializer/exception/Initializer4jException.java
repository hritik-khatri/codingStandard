package com.initializer4j.initializer.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Initializer4jException extends RuntimeException {
  private final String message;
  private final ErrorCode code;

  /** made to resolve error. */
  public Initializer4jException(final String message, ErrorCode code) {
    super(message);
    this.message = message;
    this.code = code;
  }

  public static Initializer4jException newBadRequest(String message) {
    return new Initializer4jException(message, ErrorCode.BAD_REQUEST);
  }

  public static Initializer4jException newNotFound(String message) {
    return new Initializer4jException(message, ErrorCode.NOT_FOUND);
  }

  public static Initializer4jException newConflictException(String message) {
    return new Initializer4jException(message, ErrorCode.CONFLICT);
  }

  public static Initializer4jException newMethodNotAllowedException(String message) {
    return new Initializer4jException(message, ErrorCode.METHOD_NOT_ALLOWED);
  }

  public static Initializer4jException newServiceUnavailableException(String message) {
    return new Initializer4jException(message, ErrorCode.SERVICE_UNAVAILABLE);
  }

  public static Initializer4jException newInvalidException(String response) {
    return new Initializer4jException(response, ErrorCode.INVALID);
  }
}
