package com.initializer4j.initializer.util;

import com.initializer4j.initializer.exception.Initializer4jException;
import com.initializer4j.initializer.model.dto.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestUtilTest {

  String validRequest;
  String invalidJsonRequest;
  String invalidRequest;

  @BeforeEach
  void setUp() {

    validRequest =
        "{\n"
            + "    \"empId\":\"3r30\",\n"
            + "    \"email\": \"glass@gmail.com\",\n"
            + "    \"designation\":\"6453efd\",\n"
            + "    \"salary\":\"242\"     \n"
            + "}";

    invalidRequest =
        "{\n"
            + "    \"email\": \"glassgmail\",\n"
            + "    \"designation\":\"6453efd\",\n"
            + "    \"salary\":\"242\"     \n"
            + "}";

    invalidJsonRequest =
        "{\n"
            + "    \"empId\":\"3r30\",\n"
            + "    \"email\": \"glass@gmail\",\n"
            + "    \"designation\":\"6453efd\",\n"
            + "    \"salary\":\"dsd242\"     \n"
            + "}";
  }

  @Test
  void givenValidRequest_toModelTest() {

    Person person1 = RequestUtil.toModel(validRequest, Person.class);
    Assertions.assertEquals("glass@gmail.com", person1.getEmail());
  }

  @Test
  void givenInValidRequest_toModelTest() {

    // given
    String expected =
        "{\"empId\":\"must not be empty\",\"email\":\"must be a well-formed email address\"}";

    // when
    Initializer4jException exp =
        Assertions.assertThrows(
            Initializer4jException.class, () -> RequestUtil.toModel(invalidRequest, Person.class));

    // then
    Assertions.assertEquals(expected, exp.getMessage());
  }

  @Test
  void givenInValidJsonRequest_toModelTest() {

    // given
    String expected =
        "Cannot deserialize value of type `int` from String \"dsd242\": not a valid Integer value";

    // when
    Initializer4jException exp =
        Assertions.assertThrows(
            Initializer4jException.class,
            () -> RequestUtil.toModel(invalidJsonRequest, Person.class));

    // then
    Assertions.assertEquals(expected, exp.getMessage());
  }
}
