package com.initializer4j.initializer.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class Person {

  @NotEmpty private String empId;
  @Email private String email;
  private String designation;
  private int salary;
}
