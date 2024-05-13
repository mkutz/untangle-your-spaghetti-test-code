package com.agiletestingdays.untangletestcode.unicornservice.test;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn;
import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn.ManeColor;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;

public class UnicornTestDataBuilder {

  private final SecureRandom random = new SecureRandom();

  private UUID id = randomUUID();

  private String name = randomAlphabetic(8);

  private ManeColor maneColor = ManeColor.values()[random.nextInt(ManeColor.values().length)];

  private Integer hornLength = random.nextInt(1, 101);

  private Integer hornDiameter = random.nextInt(1, 41);

  private UnicornTestDataBuilder() {}

  private static UnicornTestDataBuilder aUnicorn() {
    return new UnicornTestDataBuilder();
  }

  private LocalDate dateOfBirth =
      LocalDate.now()
          .minusDays(random.nextInt(0, 31))
          .minusMonths(random.nextInt(0, 13))
          .minusYears(random.nextInt(0, 101));

  public UnicornTestDataBuilder id(UUID id) {
    this.id = id;
    return this;
  }

  public UnicornTestDataBuilder name(String name) {
    this.name = name;
    return this;
  }

  public UnicornTestDataBuilder maneColor(ManeColor maneColor) {
    this.maneColor = maneColor;
    return this;
  }

  public UnicornTestDataBuilder hornLength(Integer hornLength) {
    this.hornLength = hornLength;
    return this;
  }

  public UnicornTestDataBuilder hornDiameter(Integer hornDiameter) {
    this.hornDiameter = hornDiameter;
    return this;
  }

  public UnicornTestDataBuilder dateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public UnicornTestDataBuilder age(int age) {
    this.dateOfBirth =
        LocalDate.now()
            .minusDays(random.nextInt(0, 31))
            .minusMonths(random.nextInt(0, 13))
            .minusYears(random.nextInt(0, age));
    return this;
  }

  public Unicorn build() {
    return new Unicorn(id, name, maneColor, hornLength, hornDiameter, dateOfBirth);
  }
}
