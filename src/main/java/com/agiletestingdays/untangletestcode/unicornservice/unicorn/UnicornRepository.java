package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UnicornRepository {

  private static final RowMapper<Unicorn> unicornRowMapper =
      (resultSet, rowNumber) ->
          new Unicorn(
              UUID.fromString(resultSet.getString("id")),
              resultSet.getString("name"),
              Unicorn.ManeColor.valueOf(resultSet.getString("mane_color")),
              resultSet.getInt("horn_length"),
              resultSet.getInt("horn_diameter"),
              resultSet.getDate("date_of_birth").toLocalDate());

  private final JdbcTemplate jdbcTemplate;

  public UnicornRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate);
  }

  Stream<Unicorn> streamAll() {
    return jdbcTemplate.queryForStream("SELECT * FROM unicorns", unicornRowMapper);
  }

  public Optional<Unicorn> findById(UUID id) {
    try {
      final var unicorn =
          jdbcTemplate.queryForObject(
              "SELECT * FROM unicorns WHERE id = uuid(?)", unicornRowMapper, id.toString());
      return Optional.ofNullable(unicorn);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public void save(Unicorn newUnicorn) {
    jdbcTemplate.update(
        """
            INSERT INTO unicorns(id, name, mane_color, horn_length, horn_diameter, date_of_birth)
                VALUES(?, ?, ?, ?, ?, ?);
            """,
        newUnicorn.id(),
        newUnicorn.name(),
        newUnicorn.maneColor().name(),
        newUnicorn.hornLength(),
        newUnicorn.hornDiameter(),
        newUnicorn.dateOfBirth());
  }
}
