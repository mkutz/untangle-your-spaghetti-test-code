package com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db;

import com.agiletestingdays.untangletestcode.unicornservice.application.port.out.UnicornStore;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UnicornStoreAdapter implements UnicornStore {

  UnicornRepository repository;

  public UnicornStoreAdapter(UnicornRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Unicorn> findById(UUID id) {
    return repository.findById(id).map(UnicornEntity::toUnicorn);
  }

  @Override
  public List<Unicorn> getAll() {
    return repository.findAll().stream().map(UnicornEntity::toUnicorn).toList();
  }

  @Override
  public Unicorn save(Unicorn unicorn) {
    return repository.save(new UnicornEntity(unicorn)).toUnicorn();
  }
}
