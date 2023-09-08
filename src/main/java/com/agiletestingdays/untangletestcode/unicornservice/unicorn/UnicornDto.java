package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UnicornDto(
    String name, String maneColor, Integer hornLength, Integer hornDiameter, String birthDay) {
  public UnicornDto(Unicorn unicorn) {
    this(
        unicorn.name(),
        unicorn.maneColor().name(),
        unicorn.hornLength(),
        unicorn.hornDiameter(),
        unicorn.dateOfBirth().format(DateTimeFormatter.BASIC_ISO_DATE));
  }
}
