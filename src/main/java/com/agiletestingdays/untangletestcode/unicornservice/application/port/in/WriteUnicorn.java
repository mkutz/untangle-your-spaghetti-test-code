package com.agiletestingdays.untangletestcode.unicornservice.application.port.in;

import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import java.util.List;

public interface WriteUnicorn {

  Unicorn createNewUnicorn(Unicorn unicorn);

  List<Unicorn> createNewUnicorns(List<Unicorn> unicorns);
}
