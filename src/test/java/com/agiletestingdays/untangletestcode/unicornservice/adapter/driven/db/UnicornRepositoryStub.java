package com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db;

import java.util.UUID;
import org.stubit.springdata.ListCrudRepositoryStub;

public class UnicornRepositoryStub extends ListCrudRepositoryStub<UnicornEntity, UUID>
    implements UnicornRepository {}
