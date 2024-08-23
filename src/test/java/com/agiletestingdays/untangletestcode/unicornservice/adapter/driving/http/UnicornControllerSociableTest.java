package com.agiletestingdays.untangletestcode.unicornservice.adapter.driving.http;

import static com.agiletestingdays.untangletestcode.unicornservice.test.UnicornTestDataBuilder.aUnicorn;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornRepositoryStub;
import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornStoreAdapter;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.ReadUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.in.WriteUnicorn;
import com.agiletestingdays.untangletestcode.unicornservice.application.port.out.UnicornStore;
import com.agiletestingdays.untangletestcode.unicornservice.application.service.UnicornService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.util.UriComponentsBuilder;

class UnicornControllerSociableTest {

  UnicornRepositoryStub unicornRepositoryStub = new UnicornRepositoryStub();
  UnicornStore unicornStore = new UnicornStoreAdapter(unicornRepositoryStub);
  UnicornService unicornService = new UnicornService(unicornStore);
  ReadUnicorn readUnicorn = unicornService;
  WriteUnicorn writeUnicorn = unicornService;
  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  UnicornController unicornController = new UnicornController(readUnicorn, writeUnicorn, validator);

  @Test
  void getAllUnicorns() {
    var unicorns =
        unicornService.createNewUnicorns(List.of(aUnicorn().build(), aUnicorn().build()));

    var unicornsResponse = unicornController.getAllUnicorns();

    assertThat(unicornsResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornsResponse.getBody())
        .hasSize(unicorns.size())
        .contains(new UnicornDto(unicorns.get(0)))
        .contains(new UnicornDto(unicorns.get(1)));
  }

  @Test
  void getSingleUnicorn() {
    var unicorn = unicornService.createNewUnicorn(aUnicorn().build());

    var unicornResponse = unicornController.getUnicorn(unicorn.id());

    assertThat(unicornResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(unicornResponse.getBody()).isNotNull().isEqualTo(new UnicornDto(unicorn));
  }

  @Test
  void getSingleUnicorn_unknown_id() {
    var unicornResponse = unicornController.getUnicorn(randomUUID());

    assertThat(unicornResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    assertThat(unicornResponse.getBody()).isNull();
  }

  @Test
  void postUnicorn() {
    var unicornDto = aUnicorn().buildDto();

    var createResponse =
        unicornController.postUnicorn(unicornDto, UriComponentsBuilder.newInstance());

    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(createResponse.getHeaders()).containsKey("Location");
    assertThat(
            unicornRepositoryStub.findById(
                UUID.fromString(((UnicornDto) createResponse.getBody()).id())))
        .isPresent();
  }
}
