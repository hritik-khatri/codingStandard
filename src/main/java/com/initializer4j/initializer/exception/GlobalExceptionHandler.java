package com.initializer4j.initializer.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Initializer4jException.class)
  public ResponseEntity<ExceptionResponse> initializer4jExceptionHandler(Initializer4jException exp)
      throws JsonProcessingException {

    String message = exp.getMessage();
    ErrorCode errorCode = exp.getCode();
    HttpStatus status = getHttpStatus(errorCode);

    ExceptionResponse response =
        ExceptionResponse.builder()
            .status(status)
            .message(
                errorCode.name().equals("INVALID")
                    ? new ObjectMapper().readValue(message, Object.class)
                    : message)
            .timeStamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(response, status);
  }

  private HttpStatus getHttpStatus(ErrorCode code) {
    switch (code.name()) {
      case "NOT_FOUND":
        return HttpStatus.NOT_FOUND;
      case "JSON_EXCEPTION":
      case "BAD_REQUEST":
        return HttpStatus.BAD_REQUEST;
      case "CONFLICT":
        return HttpStatus.CONFLICT;
      case "METHOD_NOT_ALLOWED":
        return HttpStatus.METHOD_NOT_ALLOWED;
      case "SERVICE_UNAVAILABLE":
        return HttpStatus.SERVICE_UNAVAILABLE;
      default:
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}
