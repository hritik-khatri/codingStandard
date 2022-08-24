package com.initializer4j.initializer.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionResponse {

  private Object message;
  private HttpStatus status;
  private LocalDateTime timeStamp;
}
