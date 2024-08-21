package com.agiletestingdays.untangletestcode.unicornservice.adapter.driving.http;

import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Range;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UnicornDto(
    String id,
    String name,
    String maneColor,
    @Range(min = 1, max = 100) Integer hornLength,
    @Range(min = 1, max = 40) Integer hornDiameter,
    @Past @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth) {
  public UnicornDto(Unicorn unicorn) {
    this(
        unicorn.id().toString(),
        unicorn.name(),
        unicorn.maneColor().name(),
        unicorn.hornLength(),
        unicorn.hornDiameter(),
        unicorn.dateOfBirth());
  }
}
