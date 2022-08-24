package com.initializer4j.initializer.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@RequiredArgsConstructor
class Initializer4jExceptionTest {

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void initializer4jExceptionTest() {
    Assertions.assertEquals(
        "User not found", Initializer4jException.newNotFound("User not found").getMessage());
  }

  @Test
  void globalExceptionTest() throws JsonProcessingException {

    Initializer4jException exception =
        new Initializer4jException("User not found", ErrorCode.BAD_REQUEST);

    var exceptionResponse = globalExceptionHandler.initializer4jExceptionHandler(exception);

    Assertions.assertEquals(400, exceptionResponse.getStatusCodeValue());
  }
}
