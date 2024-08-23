package com.agiletestingdays.untangletestcode.unicornservice.adapter.driving.http;

import static com.agiletestingdays.untangletestcode.unicornservice.test.UnicornTestDataBuilder.aUnicorn;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.ReadUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.WriteUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.util.UriComponentsBuilder;

class UnicornControllerMockistTest {

  ReadUnicorn readUnicornMock = mock(ReadUnicorn.class);
  WriteUnicorn writeUnicornMock = mock(WriteUnicorn.class);
  Validator validatorMock = mock(Validator.class);

  UnicornController unicornController =
      new UnicornController(readUnicornMock, writeUnicornMock, validatorMock);

  @Test
  void getAllUnicorns() {
    var unicorns = List.of(aUnicorn().build(), aUnicorn().build());
    when(readUnicornMock.getAll()).thenReturn(unicorns);

    var unicornsResponse = unicornController.getAllUnicorns();

    assertThat(unicornsResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornsResponse.getBody())
        .hasSize(unicorns.size())
        .contains(new UnicornDto(unicorns.get(0)))
        .contains(new UnicornDto(unicorns.get(1)));

    verify(readUnicornMock, times(1)).getAll();
  }

  @Test
  void getSingleUnicorn() {
    var unicorn = aUnicorn().build();
    when(readUnicornMock.getById(any(UUID.class))).thenReturn(Optional.of(unicorn));

    var unicornResponse = unicornController.getUnicorn(unicorn.id());

    assertThat(unicornResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornResponse.getBody()).isNotNull().isEqualTo(new UnicornDto(unicorn));
    verify(readUnicornMock, times(1)).getById(unicorn.id());
  }

  @Test
  void getSingleUnicorn_unknown_id() {
    when(readUnicornMock.getById(any(UUID.class))).thenReturn(Optional.empty());

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
    assertThat(createResponse.getHeaders()).containsKey("Location");
    verify(writeUnicornMock, times(1)).createNewUnicorn(any(Unicorn.class));
  }
}
