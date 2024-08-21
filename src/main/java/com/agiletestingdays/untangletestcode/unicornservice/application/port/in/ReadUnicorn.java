package com.agiletestingdays.untangletestcode.unicornservice.application.port.in;

import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadUnicorn {

  List<Unicorn> getAll();

  Optional<Unicorn> getById(UUID id);
}
