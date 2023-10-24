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
import org.springframework.web.util.UriComponentsBuilder;

class UnicornControllerTest {

  UnicornService service = mock(UnicornService.class);
  Validator validator = mock(Validator.class);

  UnicornController unicornController = new UnicornController(service, validator);

  @Test
  void getAllUnicorns() {
    var gilly =
        new Unicorn(
            UUID.fromString("351d0356-6d5e-47d5-adbb-4909058fdf2f"),
            "Gilly",
            ManeColor.RED,
            111,
            11,
            LocalDate.of(1911, 11, 11));
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

  @Test
  void getAllUnicorns_empty() {
    when(service.getAll()).thenReturn(List.of());

    var unicornsResponse = unicornController.getAllUnicorns();

    assertThat(unicornsResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornsResponse.getBody()).isEmpty();

    verify(service, times(1)).getAll();
  }

  @Test
  void getUnicorn() {
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

  @Test
  void getUnicorn_unknown_ID() {
    when(service.getById(any(UUID.class))).thenReturn(Optional.empty());

    var unicornResponse = unicornController.getUnicorn(randomUUID());

    assertThat(unicornResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    assertThat(unicornResponse.getBody()).isNull();
  }

  @Test
  void postUnicorn() {
    var gillyDto = new UnicornDto(null, "Gilly", "RED", 111, 11, LocalDate.of(1911, 11, 11));

    var createResponse =
        unicornController.postUnicorn(gillyDto, UriComponentsBuilder.newInstance());

    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(createResponse.getBody())
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("name", gillyDto.name())
        .hasFieldOrPropertyWithValue("maneColor", gillyDto.maneColor())
        .hasFieldOrPropertyWithValue("hornLength", gillyDto.hornLength())
        .hasFieldOrPropertyWithValue("hornDiameter", gillyDto.hornDiameter())
        .hasFieldOrPropertyWithValue("dateOfBirth", gillyDto.dateOfBirth());
    assertThat(createResponse.getHeaders()).containsKey("Location");
    verify(service, times(1)).createNewUnicorn(any(Unicorn.class));
  }
}
