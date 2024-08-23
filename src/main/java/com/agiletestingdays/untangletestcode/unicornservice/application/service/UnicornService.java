package com.agiletestingdays.untangletestcode.unicornservice.application.service;

import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.ReadUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.WriteUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.out.UnicornStore;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UnicornService implements ReadUnicorn, WriteUnicorn {

  private final UnicornStore store;

  public UnicornService(UnicornStore store) {
    this.store = store;
  }

  public List<Unicorn> getAll() {
    return store.getAll();
  }

  public Optional<Unicorn> getById(UUID id) {
    return store.findById(id);
  }

  public Unicorn createNewUnicorn(Unicorn newUnicorn) {
    return store.save(newUnicorn);
  }

  @Override
  public List<Unicorn> createNewUnicorns(List<Unicorn> unicorns) {
    return store.saveAll(unicorns);
  }
}
