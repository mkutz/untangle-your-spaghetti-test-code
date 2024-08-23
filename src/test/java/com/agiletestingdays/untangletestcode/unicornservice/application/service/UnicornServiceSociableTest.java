package com.agiletestingdays.untangletestcode.unicornservice.application.service;

import static com.agiletestingdays.untangletestcode.unicornservice.test.UnicornTestDataBuilder.aUnicorn;
import static org.assertj.core.api.Assertions.assertThat;

import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornRepositoryStub;
import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornStoreAdapter;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.out.UnicornStore;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import java.util.List;
import org.junit.jupiter.api.Test;

class UnicornServiceSociableTest {

  UnicornRepositoryStub unicornRepositoryStub = new UnicornRepositoryStub();
  UnicornStore store = new UnicornStoreAdapter(unicornRepositoryStub);

  UnicornService unicornService = new UnicornService(store);

  @Test
  void getAll() {
    var unicorns =
        unicornService.createNewUnicorns(List.of(aUnicorn().build(), aUnicorn().build()));

    var returnedUnicorns = unicornService.getAll();

    assertThat(returnedUnicorns).containsAll(unicorns);
  }

  @Test
  void getById() {
    var unicorn = unicornService.createNewUnicorn(aUnicorn().build());

    var returnedUnicorn = unicornService.getById(unicorn.id());

    assertThat(returnedUnicorn).isPresent().contains(unicorn);
  }

  @Test
  void createNewUnicorn() {
    Unicorn unicorn = aUnicorn().build();

    unicornService.createNewUnicorn(unicorn);

    assertThat(unicornRepositoryStub.findById(unicorn.id())).isPresent();
  }
}
