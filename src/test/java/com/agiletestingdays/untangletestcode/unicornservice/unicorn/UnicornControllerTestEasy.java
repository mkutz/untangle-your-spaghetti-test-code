package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn.ManeColor;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

class UnicornControllerTestEasy {

  UnicornService service = mock(UnicornService.class);
  Validator validator = mock(Validator.class);

  UnicornController unicornController = new UnicornController(service, validator);

  // Tangle Explanation: Creating and setting up Unicorn objects is duplicated in multiple tests.
  // Solution: Move the Unicorn creation and setup to a helper method.

  @Test
  void getAllUnicornsReturnsAListOfUnicornDtos() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    var garry =
        new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));

    var unicorns = List.of(gilly, garry);
    when(service.getAll()).thenReturn(unicorns);

    // ...rest of the test
  }

  // Solution:
  //    private Unicorn createUnicorn(String name, ManeColor maneColor, int hornLength, int
  // hornDiameter, LocalDate dateOfBirth) {
  //        return new Unicorn(randomUUID(), name, maneColor, hornLength, hornDiameter,
  // dateOfBirth);
  //    }
  //
  //    @Test
  //    void getAllUnicornsReturnsAListOfUnicornDtos() {
  //        var gilly = createUnicorn("Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
  //        var garry = createUnicorn("Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));
  //
  //        var unicorns = List.of(gilly, garry);
  //        when(service.getAll()).thenReturn(unicorns);
  //
  //        // ...rest of the test
  //    }

  // Tangle Explanation: Magic numbers (e.g., 111, 11, 99, 9) are used in object creation and are
  // not self-explanatory.
  // Solution: Replace magic numbers with named constants or variables for better code readability.

  @Test
  void getAllUnicornsReturnsAListOfUnicorns() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    var garry =
        new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));

    // ...
  }

  // Solution:
  private static final int GILLY_HORN_LENGTH = 111;
  private static final int GILLY_HORN_DIAMETER = 11;
  private static final int GARRY_HORN_LENGTH = 99;
  private static final int GARRY_HORN_DIAMETER = 9;

  //    @Test
  //    void getAllUnicornsReturnsAListOfUnicorns() {
  //        var gilly =
  //                new Unicorn(randomUUID(), "Gilly", ManeColor.RED, GILLY_HORN_LENGTH,
  // GILLY_HORN_DIAMETER, LocalDate.of(1911, 11, 11));
  //        var garry =
  //                new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, GARRY_HORN_LENGTH,
  // GARRY_HORN_DIAMETER, LocalDate.of(1912, 12, 12));
  //        var unicorns = List.of(gilly, garry);
  //        when(service.getAll()).thenReturn(unicorns);
  //
  //        // ...rest of the test
  //    }

  // Tangle Explanation: The test name "getSingleUnicornReturnsValidJson" is not very descriptive
  // and does not provide clear information about the purpose of the test.
  // Solution: Use a more descriptive and meaningful test name to improve understanding of the
  // test's intent.

  @Test
  void getSingleUnicornReturnsValidJson() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    when(service.getById(any(UUID.class))).thenReturn(Optional.of(gilly));

    var unicornResponse = unicornController.getUnicorn(gilly.id());

    assertThat(unicornResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornResponse.getBody())
        .isNotNull()
        .isEqualTo(
            new UnicornDto(
                gilly.id().toString(),
                gilly.name(),
                gilly.maneColor().toString(),
                gilly.hornLength(),
                gilly.hornDiameter(),
                gilly.dateOfBirth()));
    verify(service, times(1)).getById(gilly.id());
  }

  // Solution:
  @Test
  void shouldReturnValidJsonForSingleUnicorn() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    when(service.getById(any(UUID.class))).thenReturn(Optional.of(gilly));

    var unicornResponse = unicornController.getUnicorn(gilly.id());

    assertThat(unicornResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornResponse.getBody())
        .isNotNull()
        .isEqualTo(
            new UnicornDto(
                gilly.id().toString(),
                gilly.name(),
                gilly.maneColor().toString(),
                gilly.hornLength(),
                gilly.hornDiameter(),
                gilly.dateOfBirth()));
    verify(service, times(1)).getById(gilly.id());
  }

  // Tangle Explanation: Hardcoded data values (e.g., "200") are used in assertions, making the test
  // less flexible and more prone to breaking if the expected values change.
  // Solution: Use constants or variables to store expected values in assertions to make the test
  // more adaptable to changes.

  @Test
  void getAllUnicornsReturnsAListOfUnicornDtosFoo() {
    // ... previous test setup code

    //        assertThat(unicornsResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    //        assertThat(unicornsResponse.getBody())
    //                .hasSize(2)
    //                .contains(
    //                        new UnicornDto(
    //                                gilly.id().toString(),
    //                                gilly.name(),
    //                                gilly.maneColor().toString(),
    //                                gilly.hornLength(),
    //                                gilly.hornDiameter(),
    //                                gilly.dateOfBirth()))
    //                .contains(
    //                        new UnicornDto(
    //                                garry.id().toString(),
    //                                garry.name(),
    //                                garry.maneColor().toString(),
    //                                garry.hornLength(),
    //                                garry.hornDiameter(),
    //                                garry.dateOfBirth()));

    // ... rest of the test
  }

  // Solution:
  //    private static final int EXPECTED_OK_STATUS_CODE = 200;
  //
  //    @Test
  //    void getAllUnicornsReturnsAListOfUnicornDtos() {
  //        // ... previous test setup code
  //
  //
  // assertThat(unicornsResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(EXPECTED_OK_STATUS_CODE));
  //        assertThat(unicornsResponse.getBody())
  //                .hasSize(2)
  //                .contains(
  //                        new UnicornDto(
  //                                gilly.id().toString(),
  //                                gilly.name(),
  //                                gilly.maneColor().toString(),
  //                                gilly.hornLength(),
  //                                gilly.hornDiameter(),
  //                                gilly.dateOfBirth()))
  //                .contains(
  //                        new UnicornDto(
  //                                garry.id().toString(),
  //                                garry.name(),
  //                                garry.maneColor().toString(),
  //                                garry.hornLength(),
  //                                garry.hornDiameter(),
  //                                garry.dateOfBirth()));
  //
  //        // ... rest of the test
  //    }

  // Tangle Explanation: The test method "getAllUnicornsReturnsAListOfUnicornDtos" is testing both
  // the HTTP response and the behavior of the service/mock. It's testing two things in a single
  // test.
  // Solution: Split the test into two separate tests, one for testing the HTTP response and another
  // for testing the behavior of the service/mock.

  @Test
  void getAllUnicornsReturnsAListOfUnicornDtosBar() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    var garry =
        new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));
    var unicorns = List.of(gilly, garry);
    when(service.getAll()).thenReturn(unicorns);

    var unicornsResponse = unicornController.getAllUnicorns();

    assertThat(unicornsResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornsResponse.getBody())
        .hasSize(2)
        .contains(
            new UnicornDto(
                gilly.id().toString(),
                gilly.name(),
                gilly.maneColor().toString(),
                gilly.hornLength(),
                gilly.hornDiameter(),
                gilly.dateOfBirth()))
        .contains(
            new UnicornDto(
                garry.id().toString(),
                garry.name(),
                garry.maneColor().toString(),
                garry.hornLength(),
                garry.hornDiameter(),
                garry.dateOfBirth()));

    verify(service, times(1)).getAll();
  }

  // Solution:

  // Test for HTTP response
  @Test
  void getAllUnicornsReturnsHttpResponse() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    var garry =
        new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));
    var unicorns = List.of(gilly, garry);
    when(service.getAll()).thenReturn(unicorns);

    var unicornsResponse = unicornController.getAllUnicorns();

    assertThat(unicornsResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornsResponse.getBody()).hasSize(2);
  }

  // Test for service/mock behavior
  @Test
  void getAllUnicornsCallsService() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    var garry =
        new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));
    var unicorns = List.of(gilly, garry);
    when(service.getAll()).thenReturn(unicorns);

    unicornController.getAllUnicorns();

    verify(service, times(1)).getAll();
  }
}
