package com.agiletestingdays.untangletestcode.unicornservice.application.service;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiletestingdays.untangletestcode.unicornservice.application.port.out.UnicornStore;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn.ManeColor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UnicornServiceTest {

  UnicornStore store = mock(UnicornStore.class);

  UnicornService unicornService = new UnicornService(store);

  @Test
  void getAllCallsRepository() {
    var unicorns =
        List.of(
            new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11)),
            new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12)));
    when(store.getAll()).thenReturn(unicorns);

    var returnedUnicorns = unicornService.getAll();

    assertThat(returnedUnicorns).containsAll(unicorns);
    verify(store, times(1)).getAll();
  }

  @Test
  void getByIdCallsRepository() {
    var gilly =
        new Unicorn(randomUUID(), "Gilly", ManeColor.RED, 111, 11, LocalDate.of(1911, 11, 11));
    when(store.findById(any(UUID.class))).thenReturn(Optional.of(gilly));

    var returnedUnicorn = unicornService.getById(gilly.id());

    assertThat(returnedUnicorn).isPresent().contains(gilly);
    verify(store, times(1)).findById(gilly.id());
  }

  @Test
  void savingAUnicornWorks() {
    Unicorn garry =
        new Unicorn(randomUUID(), "Garry", ManeColor.BLUE, 99, 9, LocalDate.of(1912, 12, 12));
    when(store.save(garry)).thenAnswer(invocation -> invocation.getArgument(0));

    unicornService.createNewUnicorn(garry);

    verify(store, times(1)).save(garry);
  }
}
