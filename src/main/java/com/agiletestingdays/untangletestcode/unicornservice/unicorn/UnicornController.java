package com.agiletestingdays.untangletestcode.unicornservice.unicorn;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("unicorns")
public class UnicornController {

  private final UnicornService service;

  public UnicornController(UnicornService service) {
    this.service = requireNonNull(service);
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
  public ResponseEntity<UnicornDto> postUnicorn() {
    return ResponseEntity.ok().build();
  }
}
