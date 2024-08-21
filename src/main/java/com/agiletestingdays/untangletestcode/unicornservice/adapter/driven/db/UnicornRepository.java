package com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnicornRepository extends ListCrudRepository<UnicornEntity, UUID> {

  List<UnicornEntity> findAll();
}
