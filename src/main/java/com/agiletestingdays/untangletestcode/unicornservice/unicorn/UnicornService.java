package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UnicornService {

  private final UnicornRepository repository;

  public UnicornService(UnicornRepository repository) {
    this.repository = repository;
  }

  public List<Unicorn> getAll() {
    return repository.streamAll().toList();
  }

  public Optional<Unicorn> getById(UUID id) {
    return repository.findById(id);
  }

  public void createNewUnicorn(Unicorn newUnicorn) {
    repository.save(newUnicorn);
  }
}
