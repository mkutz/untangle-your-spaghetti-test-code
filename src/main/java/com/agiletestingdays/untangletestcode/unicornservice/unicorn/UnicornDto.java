package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UnicornDto(
    String id,
    String name,
    String maneColor,
    Integer hornLength,
    Integer hornDiameter,
    String dateOfBirth) {
  public UnicornDto(Unicorn unicorn) {
    this(
        unicorn.id().toString(),
        unicorn.name(),
        unicorn.maneColor().name(),
        unicorn.hornLength(),
        unicorn.hornDiameter(),
        unicorn.dateOfBirth().format(DateTimeFormatter.ISO_DATE));
  }
}
