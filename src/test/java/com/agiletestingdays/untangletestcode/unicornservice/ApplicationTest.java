package com.agiletestingdays.untangletestcode.unicornservice;

import static com.agiletestingdays.untangletestcode.unicornservice.test.UnicornTestDataBuilder.aUnicorn;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.post;

import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornEntity;
import com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db.UnicornRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = LocalTestApplication.class)
class ApplicationTest {

  @Value("http://localhost:${local.server.port}")
  String baseUrl;

  @Autowired TestRestTemplate restTemplate;
  @Autowired ObjectMapper objectMapper;
  @Autowired UnicornRepository unicornRepository;

  // DONE resolve inconsistent test case names ✔
  // TODO move special cases to unit tests

  @BeforeEach
  void clearDatabase() {
    unicornRepository.deleteAll();
  }

  @Test
  @DisplayName("GET /unicorns")
  void getAllUnicorns() throws JsonProcessingException {
    // DONE resolve hidden arrange ✔
    // ARRANGE
    List<UnicornEntity> unicorns =
        unicornRepository.saveAll(List.of(aUnicorn().buildEntity(), aUnicorn().buildEntity()));

    // ACT
    var response = restTemplate.getForEntity("%s/unicorns".formatted(baseUrl), String.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    var body = objectMapper.readTree(response.getBody());
    assertThat(body).isNotNull();
    assertThat(body.isArray()).isTrue();
    assertThat(body.size()).isEqualTo(unicorns.size());
  }

  @Test
  @DisplayName("GET /unicorns/:id")
  void getUnicorn() throws JsonProcessingException {
    // DONE resolve hidden arrange ✔
    // ARRANGE
    UnicornEntity unicorn = unicornRepository.save(aUnicorn().buildEntity());

    // ACT
    var response =
        restTemplate.getForEntity(
            "%s/unicorns/%s".formatted(baseUrl, unicorn.getId()), String.class);

    // ASSERT
    // TODO resolve long assert
    var unicornData = objectMapper.readTree(response.getBody());

    // DONE resolve magic values below ✔
    assertThat(unicornData.has("id")).isTrue();
    assertThat(unicornData.get("id").asText()).isEqualTo(unicorn.getId().toString());

    assertThat(unicornData.has("name")).isTrue();
    assertThat(unicornData.get("name").asText()).isEqualTo(unicorn.getName());

    assertThat(unicornData.has("maneColor")).isTrue();
    assertThat(unicornData.get("maneColor").asText()).isEqualTo(unicorn.getManeColor());

    assertThat(unicornData.has("hornLength")).isTrue();
    assertThat(unicornData.get("hornLength").asInt()).isEqualTo(unicorn.getHornLength());

    assertThat(unicornData.has("hornDiameter")).isTrue();
    assertThat(unicornData.get("hornDiameter").asInt()).isEqualTo(unicorn.getHornDiameter());

    assertThat(unicornData.has("dateOfBirth")).isTrue();
    assertThat(unicornData.get("dateOfBirth").asText())
        .isEqualTo(LocalDate.ofInstant(unicorn.getDateOfBirth(), ZoneOffset.UTC).toString());
  }

  @Test
  // @DirtiesContext // DONE replace with DB reset in setup ✔
  @DisplayName("POST /unicorns")
  void postUnicorn() {
    // ARRANGE
    var garryJson = aUnicorn().buildJson();

    // ACT
    // TODO resolve long/technical act
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(garryJson),
            String.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(response.getHeaders().get("Location")).isNotNull().hasSize(1);

    // TODO resolve multiple acts
    // ACT 2
    var anotherResponse =
        restTemplate.getForEntity(
            requireNonNull(response.getHeaders().get("Location")).getFirst(), String.class);

    // ASSERT 2
    assertThat(anotherResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
  }

  @Test
  // @DirtiesContext // DONE replace with DB reset in setup ✔
  @DisplayName("POST /unicorns hornLength too low")
  void postUnicorn_hornLength_too_low() {
    // ARRANGE
    // DONE resolve long arrange ✔
    var larryJson = aUnicorn().hornLength(0).buildJson();

    // ACT
    // TODO resolve long/technical act
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(larryJson),
            List.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody()).contains("hornLength must be between 1 and 100");
  }

  @Test
  // @DirtiesContext // DONE replace with DB reset in setup ✔
  @DisplayName("POST /unicorns hornLength too great")
  void postUnicorn_hornLength_too_great() {
    // ARRANGE
    // DONE resolve long arrange ✔
    var larryJson = aUnicorn().hornLength(101).buildJson();

    // ACT
    // TODO resolve long/technical act
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(larryJson),
            List.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody()).contains("hornLength must be between 1 and 100");
  }

  @Test
  // @DirtiesContext // DONE replace with DB reset in setup ✔
  @DisplayName("POST /unicorns hornDiameter too low")
  void postUnicorn_hornDiameter_too_low() { // DONE resolve lying test name ✔
    // ARRANGE
    // DONE resolve long arrange ✔
    var larryJson = aUnicorn().hornDiameter(0).buildJson();

    // ACT
    // TODO resolve long/technical act
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(larryJson),
            List.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody()).contains("hornDiameter must be between 1 and 40");
  }

  @Test
  // @DirtiesContext // DONE replace with DB reset in setup ✔
  @DisplayName("POST /unicorns hornDiameter too great")
  void postUnicorn_hornDiameter_too_great() throws JsonProcessingException {
    // ARRANGE
    // DONE resolve long arrange ✔
    var larryJson = aUnicorn().hornDiameter(42).buildJson();

    // ACT
    // TODO resolve long/technical act
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(larryJson),
            List.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody()).contains("hornDiameter must be between 1 and 40");
  }

  @Test
  // @DirtiesContext // DONE replace with DB reset in setup ✔
  @DisplayName("POST /unicorns dateOfBirth future")
  void postUnicorn_dateOfBirth_future() {
    // ARRANGE
    // DONE resolve long arrange ✔
    var larryJson = aUnicorn().dateOfBirth(LocalDate.now().plusDays(1)).buildJson();

    // ACT
    // TODO resolve long/technical act
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .header("Accept-Language", "en")
                .body(larryJson),
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody()).contains("dateOfBirth must be a past date");
  }
}
