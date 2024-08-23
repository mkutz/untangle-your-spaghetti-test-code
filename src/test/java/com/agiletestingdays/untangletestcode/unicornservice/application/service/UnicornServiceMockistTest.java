package com.agiletestingdays.untangletestcode.unicornservice.application.service;

import static com.agiletestingdays.untangletestcode.unicornservice.test.UnicornTestDataBuilder.aUnicorn;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiletestingdays.untangletestcode.unicornservice.application.port.out.UnicornStore;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UnicornServiceMockistTest {

  UnicornStore unicornStoreMock = mock(UnicornStore.class);

  UnicornService unicornService = new UnicornService(unicornStoreMock);

  @Test
  void getAll() {
    var unicorns = List.of(aUnicorn().build(), aUnicorn().build());
    when(unicornStoreMock.getAll()).thenReturn(unicorns);

    var returnedUnicorns = unicornService.getAll();

    assertThat(returnedUnicorns).containsAll(unicorns);
    verify(unicornStoreMock, times(1)).getAll();
  }

  @Test
  void getById() {
    var unicorn = aUnicorn().build();
    when(unicornStoreMock.findById(any(UUID.class))).thenReturn(Optional.of(unicorn));

    var returnedUnicorn = unicornService.getById(unicorn.id());

    assertThat(returnedUnicorn).isPresent().contains(unicorn);
    verify(unicornStoreMock, times(1)).findById(unicorn.id());
  }

  @Test
  void createNewUnicorn() {
    Unicorn unicorn = aUnicorn().build();
    when(unicornStoreMock.save(unicorn)).thenAnswer(invocation -> invocation.getArgument(0));

    unicornService.createNewUnicorn(unicorn);

    verify(unicornStoreMock, times(1)).save(unicorn);
  }
}
