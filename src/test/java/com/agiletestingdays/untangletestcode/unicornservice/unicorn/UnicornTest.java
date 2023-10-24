package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.agiletestingdays.untangletestcode.unicornservice.UnicornTestDataBuilder;
import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn.ManeColor;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class UnicornTest {

  UnicornTestDataBuilder unicornTestDataBuilder = new UnicornTestDataBuilder();

  @Test
  void age_birthday() {
    var gilly = unicornTestDataBuilder.dateOfBirth(LocalDate.now().minusYears(62)).build();

    assertThat(gilly.age()).isEqualTo(62);
  }

  @Test
  void age_birthday_2_days_ago() {
    var gilly =
        unicornTestDataBuilder
            .dateOfBirth(LocalDate.now().minusYears(62).minusMonths(1).minusDays(2))
            .build();

    assertThat(gilly.age()).isEqualTo(62);
  }

  @Test
  void age_birthday_tomorrow() {
    var gilly =
        unicornTestDataBuilder.dateOfBirth(LocalDate.now().minusYears(62).plusDays(1)).build();

    assertThat(gilly.age()).isEqualTo(61);
  }

  @Test
  void age_future_birthday() {
    try {
      new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.now().plusYears(2));
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo("Future dates of birth are not supported!");
    }
  }
}
