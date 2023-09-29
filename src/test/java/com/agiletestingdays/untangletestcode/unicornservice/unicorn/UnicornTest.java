package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn.ManeColor;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class UnicornTest {

  @Test
  void ageWorks() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.now().minusYears(62));

    assertThat(gilly.age()).isEqualTo(62);
  }

  @Test
  void ageWorksHereToo() {
    var gilly =
        new Unicorn(
            randomUUID(),
            "Gilly",
            ManeColor.RED,
            111,
            11,
            LocalDate.now().minusYears(62).minusMonths(1).minusDays(2));

    assertThat(gilly.age()).isEqualTo(62);
  }

  @Test
  void ageWorksHereAlso() {
    var gilly =
        new Unicorn(
            randomUUID(),
            "Gilly",
            ManeColor.RED,
            111,
            11,
            LocalDate.now().minusYears(62).plusDays(1));

    assertThat(gilly.age()).isEqualTo(61);
  }

  @Test
  void negativeAge() {
    try {
      new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.now().plusYears(2));
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("Future dates of birth are not supported!");
    }
  }
}
