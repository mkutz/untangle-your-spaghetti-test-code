package com.agiletestingdays.untangletestcode.unicornservice.test;

import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestDataManager {

  private final JdbcTemplate jdbcTemplate;

  public TestDataManager(DataSource daraSource) {
    jdbcTemplate = new JdbcTemplate(daraSource);
  }

  public TestDataManager withUnicorn(Unicorn unicorn) {
    jdbcTemplate.update(
        """
            INSERT
              INTO
                  unicorns(
                      id,
                      name,
                      mane_color,
                      horn_length,
                      horn_diameter,
                      date_of_birth
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
    jdbcTemplate.execute("TRUNCATE TABLE unicorns;");
    return this;
  }
}
