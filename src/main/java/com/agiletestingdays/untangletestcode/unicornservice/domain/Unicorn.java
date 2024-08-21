package com.agiletestingdays.untangletestcode.unicornservice.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public record Unicorn(
    UUID id,
    String name,
    ManeColor maneColor,
    Integer hornLength,
    Integer hornDiameter,
    LocalDate dateOfBirth) {

  public Unicorn {
    if (dateOfBirth.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Future dates of birth are not supported!");
    }
  }

  public Integer age() {
    return Period.between(dateOfBirth, LocalDate.now()).getYears();
  }

  public enum ManeColor {
    BLUE,
    RED,
    GREEN,
    RAINBOW
  }
}
