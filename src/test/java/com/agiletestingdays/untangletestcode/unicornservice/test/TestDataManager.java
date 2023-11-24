package com.agiletestingdays.untangletestcode.unicornservice.test;

import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestDataManager {

  private final JdbcTemplate jdbcTemplate;

  public TestDataManager(DataSource daraSource) {
    this.jdbcTemplate = new JdbcTemplate(daraSource);
  }

  public TestDataManager withUnicorn(Unicorn unicorn) {
    jdbcTemplate.update(
        """
            INSERT
              INTO
                  UNICORNS(
                      ID,
                      NAME,
                      MANE_COLOR,
                      HORN_LENGTH,
                      HORN_DIAMETER,
                      DATE_OF_BIRTH
                  )
              VALUES(
                  ?,
                  ?,
                  ?,
                  ?,
                  ?,
                  ?
              );
            """,
        unicorn.id(),
        unicorn.name(),
        unicorn.maneColor().name(),
        unicorn.hornLength(),
        unicorn.hornDiameter(),
        unicorn.dateOfBirth());
    return this;
  }

  public TestDataManager clear() {
    jdbcTemplate.execute("TRUNCATE TABLE UNICORNS;");
    return this;
  }
}
