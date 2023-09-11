package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.agiletestingdays.untangletestcode.unicornservice.unicorn.Unicorn.ManeColor;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController("unicorns")
public class UnicornController {

  private final UnicornService service;
  private final Validator validator;

  public UnicornController(UnicornService service, Validator validator) {
    this.service = requireNonNull(service);
    this.validator = requireNonNull(validator);
  }

  @GetMapping(
      path = {"unicorns", "unicorns/"},
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UnicornDto>> getAllUnicorns() {
    return ResponseEntity.ok(service.getAll().stream().map(UnicornDto::new).toList());
  }

  @GetMapping(path = "unicorns/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UnicornDto> getUnicorn(@PathVariable UUID id) {
    return ResponseEntity.ofNullable(service.getById(id).map(UnicornDto::new).orElse(null));
  }

  @PostMapping(
      path = {"unicorns", "unicorns/"},
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UnicornDto> postUnicorn(
      @RequestBody UnicornDto dto, UriComponentsBuilder uriComponentsBuilder) {
    validator.validate(dto);
    var unicornId = randomUUID();
    var unicorn =
        new Unicorn(
            unicornId,
            dto.name(),
            ManeColor.valueOf(dto.maneColor()),
            dto.hornLength(),
            dto.hornDiameter(),
            LocalDate.parse(dto.dateOfBirth()));
    service.createNewUnicorn(unicorn);
    return ResponseEntity.created(
            uriComponentsBuilder.path("unicorns/").path(unicornId.toString()).build().toUri())
        .body(new UnicornDto(unicorn));
  }
}
