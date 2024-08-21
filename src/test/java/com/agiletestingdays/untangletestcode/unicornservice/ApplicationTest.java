package com.agiletestingdays.untangletestcode.unicornservice;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = LocalTestApplication.class)
class ApplicationTest {

  @Value("http://localhost:${local.server.port}")
  String baseUrl;

  @Autowired TestRestTemplate restTemplate;
  @Autowired ObjectMapper objectMapper;

  // DONE resolve inconsistent test case names ✔
  // TODO move special cases to unit tests

  @Test
  @DisplayName("GET /unicorns")
  void getAllUnicorns() throws JsonProcessingException {
    // TODO resolve hidden arrange
    // ACT
    var response = restTemplate.getForEntity("%s/unicorns".formatted(baseUrl), String.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    var body = objectMapper.readTree(response.getBody());
    assertThat(body).isNotNull();
    assertThat(body.isArray()).isTrue();
    assertThat(body.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("GET /unicorns/:id")
  void getUnicorn() throws JsonProcessingException {
    // TODO resolve hidden arrange
    // ACT
    var response =
        restTemplate.getForEntity(
            "%s/unicorns/%s".formatted(baseUrl, "44eb6bdc-a0c9-4ce4-b28b-86d5950bcd23"),
            String.class);

    // ASSERT
    // TODO resolve long assert
    var unicornData = objectMapper.readTree(response.getBody());

    // TODO resolve magic values below
    assertThat(unicornData.has("id")).isTrue();
    assertThat(unicornData.get("id").asText()).isEqualTo("44eb6bdc-a0c9-4ce4-b28b-86d5950bcd23");

    assertThat(unicornData.has("name")).isTrue();
    assertThat(unicornData.get("name").asText()).isEqualTo("Grace");

    assertThat(unicornData.has("maneColor")).isTrue();
    assertThat(unicornData.get("maneColor").asText()).isEqualTo("RAINBOW");

    assertThat(unicornData.has("hornLength")).isTrue();
    assertThat(unicornData.get("hornLength").asInt()).isEqualTo(42);

    assertThat(unicornData.has("hornDiameter")).isTrue();
    assertThat(unicornData.get("hornDiameter").asInt()).isEqualTo(10);

    assertThat(unicornData.has("dateOfBirth")).isTrue();
    assertThat(unicornData.get("dateOfBirth").asText()).isEqualTo("1982-02-19");
  }

  @Test
  @DirtiesContext // TODO replace with DB reset in setup
  @DisplayName("POST /unicorns")
  void postUnicorn() {
    // ARRANGE
    var garryJson =
        "{\"dateOfBirth\":\"1999-10-12\",\"hornDiameter\":11,\"hornLength\":37,\"maneColor\":\"BLUE\",\"name\":\"Garry\"}";

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
  @DirtiesContext // TODO replace with DB reset in setup
  @DisplayName("POST /unicorns hornLength too low")
  void postUnicorn_hornLength_too_low() throws JsonProcessingException {
    // ARRANGE
    // TODO resolve long arrange
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Larry",
                "maneColor",
                "BLUE",
                "hornLength",
                0, // only this matters
                "hornDiameter",
                18,
                "dateOfBirth",
                "1999-10-12"));

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
  @DirtiesContext // TODO replace with DB reset in setup
  @DisplayName("POST /unicorns hornLength too great")
  void postUnicorn_hornLength_too_great() throws JsonProcessingException {
    // ARRANGE
    // TODO resolve long arrange
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Barry",
                "maneColor",
                "BLUE",
                "hornLength",
                101, // only this matters
                "hornDiameter",
                18,
                "dateOfBirth",
                "1999-10-12"));

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
  @DirtiesContext // TODO replace with DB reset in setup
  @DisplayName("POST /unicorns hornDiameter too low")
  void postUnicorn_hornDiameter_too_low()
      throws JsonProcessingException { // TODO resolve lying test name
    // ARRANGE
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Jerry",
                "maneColor",
                "BLUE",
                "hornLength",
                66,
                "hornDiameter",
                0, // only this matters
                "dateOfBirth",
                "1999-10-12"));

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
  @DirtiesContext // TODO replace with DB reset in setup
  @DisplayName("POST /unicorns hornDiameter too great")
  void postUnicorn_hornDiameter_too_great() throws JsonProcessingException {
    // ARRANGE
    // TODO resolve long arrange
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Harry",
                "maneColor",
                "BLUE",
                "hornLength",
                67,
                "hornDiameter",
                42, // only this matters
                "dateOfBirth",
                "1999-10-12"));

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
  @DirtiesContext // TODO replace with DB reset in setup
  @DisplayName("POST /unicorns dateOfBirth future")
  void postUnicorn_dateOfBirth_future() throws JsonProcessingException {
    // ARRANGE
    // TODO resolve long arrange
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Mary",
                "maneColor",
                "BLUE",
                "hornLength",
                37,
                "hornDiameter",
                11,
                "dateOfBirth",
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE)) // only this matters
            );

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
