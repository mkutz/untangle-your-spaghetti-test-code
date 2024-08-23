package com.agiletestingdays.untangletestcode.unicornservice.application.port.out;

import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnicornStore {

  Optional<Unicorn> findById(UUID id);

  List<Unicorn> getAll();

  Unicorn save(Unicorn unicorn);

  List<Unicorn> saveAll(List<Unicorn> unicorns);
}
