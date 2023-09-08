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
              UUID.fromString(resultSet.getString("ID")),
              resultSet.getString("NAME"),
              Unicorn.ManeColor.valueOf(resultSet.getString("MANE_COLOR")),
              resultSet.getInt("HORN_LENGTH"),
              resultSet.getInt("HORN_DIAMETER"),
              resultSet.getDate("DATE_OF_BIRTH").toLocalDate());

  private final JdbcTemplate jdbcTemplate;

  public UnicornRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate);
  }

  Stream<Unicorn> streamAll() {
    return jdbcTemplate.queryForStream("SELECT * FROM UNICORNS", unicornRowMapper);
  }

  public Optional<Unicorn> findById(UUID id) {
    try {
      final var unicorn =
          jdbcTemplate.queryForObject(
              "SELECT * FROM UNICORNS WHERE ID = ?", unicornRowMapper, id.toString());
      return Optional.ofNullable(unicorn);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
