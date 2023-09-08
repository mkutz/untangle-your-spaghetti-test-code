package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import java.time.LocalDate;
import java.util.UUID;

public record Unicorn(
    UUID id,
    String name,
    ManeColor maneColor,
    Integer hornLength,
    Integer hornDiameter,
    LocalDate dateOfBirth) {

  public enum ManeColor {
    BLUE,
    RED,
    GREEN,
    RAINBOW
  }
}
