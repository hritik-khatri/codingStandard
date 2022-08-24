package com.initializer4j.initializer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializer4j.initializer.exception.ErrorCode;
import com.initializer4j.initializer.exception.Initializer4jException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Set;

public class RequestUtil {

  RequestUtil(){}

  public static <T> T toModel(String user, Class<T> valueType) {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    ObjectMapper om = new ObjectMapper();
    try {
      var request = om.readValue(user, valueType);

      Set<ConstraintViolation<T>> violations = validator.validate(request);

      if (!violations.isEmpty()) {
        HashMap<String, String> response = new HashMap<>();
        for (ConstraintViolation<T> violation : violations) {
          response.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        throw Initializer4jException.newInvalidException(
            new ObjectMapper().writeValueAsString(response));
      }
      return request;
    } catch (Initializer4jException exception) {
      throw Initializer4jException.newInvalidException(exception.getMessage());
    } catch (Exception exception) {
      throw new Initializer4jException(
          exception.getMessage().split("\\r?\\n")[0], ErrorCode.JSON_EXCEPTION);
    }
  }
}
