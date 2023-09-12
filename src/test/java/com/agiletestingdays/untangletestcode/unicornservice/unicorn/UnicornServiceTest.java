package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn.ManeColor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UnicornServiceTest {

  UnicornRepository repository = mock(UnicornRepository.class);

  UnicornService unicornService = new UnicornService(repository);

  @Test
  void getAllCallsRepository() {
    var unicorns =
        List.of(
            new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11)),
            new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12)));
    when(repository.streamAll()).thenReturn(unicorns.stream());

    var returnedUnicorns = unicornService.getAll();

    assertThat(returnedUnicorns).containsAll(unicorns);
    verify(repository, times(1)).streamAll();
  }

  @Test
  void getByIdCallsRepository() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    when(repository.findById(any(UUID.class))).thenReturn(Optional.of(gilly));

    var returnedUnicorn = unicornService.getById(gilly.id());

    assertThat(returnedUnicorn).isPresent().contains(gilly);
    verify(repository, times(1)).findById(gilly.id());
  }

  @Test
  void savingAUnicornWorks() {
    Unicorn garry =
        new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));

    unicornService.createNewUnicorn(garry);

    verify(repository, times(1)).save(garry);
  }
}
