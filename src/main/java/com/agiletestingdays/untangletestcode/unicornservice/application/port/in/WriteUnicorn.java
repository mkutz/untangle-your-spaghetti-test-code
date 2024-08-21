package com.agiletestingdays.untangletestcode.unicornservice.application.port.in;

import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;

public interface WriteUnicorn {

  void createNewUnicorn(Unicorn unicorn);
}
