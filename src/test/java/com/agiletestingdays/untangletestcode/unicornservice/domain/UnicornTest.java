package com.agiletestingdays.untangletestcode.unicornservice.domain;

import static com.agiletestingdays.untangletestcode.unicornservice.test.UnicornTestDataBuilder.aUnicorn;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class UnicornTest {

  @Test
  void age_exact() {
    var unicorn = aUnicorn().dateOfBirth(LocalDate.now().minusYears(62)).build();

    assertThat(unicorn.age()).isEqualTo(62);
  }

  @Test
  void age_older() {
    var unicorn =
        aUnicorn().dateOfBirth(LocalDate.now().minusYears(62).minusMonths(1).minusDays(2)).build();

    assertThat(unicorn.age()).isEqualTo(62);
  }

  @Test
  void age_younger() {
    var unicorn = aUnicorn().dateOfBirth(LocalDate.now().minusYears(62).plusDays(1)).build();

    assertThat(unicorn.age()).isEqualTo(61);
  }

  @Test
  void constructor_future_dateOfBirth() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> aUnicorn().dateOfBirth(LocalDate.now().plusYears(2)).build())
        .withMessage("Future dates of birth are not supported!");
  }
}
