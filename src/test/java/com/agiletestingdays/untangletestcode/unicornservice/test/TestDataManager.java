package com.agiletestingdays.untangletestcode.unicornservice.test;

import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornEntity;
import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornRepository;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TestDataManager {

  private final UnicornRepository repository;

  public TestDataManager(@Qualifier("unicornRepository") UnicornRepository repository) {
    this.repository = repository;
  }

  public TestDataManager withUnicorn(Unicorn unicorn) {
    repository.save(new UnicornEntity(unicorn));
    return this;
  }

  public TestDataManager clear() {
    repository.deleteAll();
    return this;
  }
}
