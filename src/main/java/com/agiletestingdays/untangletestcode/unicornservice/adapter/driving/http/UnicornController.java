package com.agiletestingdays.untangletestcode.unicornservice.adapter.driving.http;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.ReadUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.WriteUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn.ManeColor;
import jakarta.validation.Validator;
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

  private final ReadUnicorn readUnicorn;
  private final WriteUnicorn writeUnicorn;
  private final Validator validator;

  public UnicornController(
      ReadUnicorn readUnicorn, WriteUnicorn writeUnicorn, Validator validator) {
    this.readUnicorn = readUnicorn;
    this.writeUnicorn = writeUnicorn;
    this.validator = validator;
  }

  @GetMapping(
      path = {"unicorns", "unicorns/"},
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UnicornDto>> getAllUnicorns() {
    return ResponseEntity.ok(readUnicorn.getAll().stream().map(UnicornDto::new).toList());
  }

  @GetMapping(path = "unicorns/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UnicornDto> getUnicorn(@PathVariable("id") UUID id) {
    return ResponseEntity.ofNullable(readUnicorn.getById(id).map(UnicornDto::new).orElse(null));
  }

  @PostMapping(
      path = {"unicorns", "unicorns/"},
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postUnicorn(
      @RequestBody UnicornDto dto, UriComponentsBuilder uriComponentsBuilder) {
    var validationResult = validator.validate(dto);
    if (!validationResult.isEmpty()) {
      var violationMessages =
          validationResult.stream()
              .map(
                  violation ->
                      "%s %s".formatted(violation.getPropertyPath(), violation.getMessage()))
              .toList();
      return ResponseEntity.badRequest().body(violationMessages);
    }

    var unicornId = randomUUID();
    var unicorn =
        new Unicorn(
            unicornId,
            dto.name(),
            ManeColor.valueOf(dto.maneColor()),
            dto.hornLength(),
            dto.hornDiameter(),
            dto.dateOfBirth());
    writeUnicorn.createNewUnicorn(unicorn);

    return ResponseEntity.created(
            uriComponentsBuilder.path("unicorns/").path(unicornId.toString()).build().toUri())
        .body(new UnicornDto(unicorn));
  }
}
