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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UnicornServiceApplicationTests {

  @Value("http://localhost:${local.server.port}")
  String baseUrl;

  @Autowired TestRestTemplate restTemplate;
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void getUnicornsWorksAndReturnsNonEmptyList() throws JsonProcessingException {
    var response = restTemplate.getForEntity("%s/unicorns".formatted(baseUrl), String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    var body = objectMapper.readTree(response.getBody());

    assertThat(body).isNotNull();
    assertThat(body.isArray()).isTrue();
    assertThat(body.size()).isEqualTo(1);
  }

  @Test
  void getSingleUnicornWorksAndReturnsData() throws JsonProcessingException {
    var response =
        restTemplate.getForEntity(
            "%s/unicorns/%s".formatted(baseUrl, "44eb6bdc-a0c9-4ce4-b28b-86d5950bcd23"),
            String.class);
    var unicornData = objectMapper.readTree(response.getBody());

    // LONG ASSERT + MAGIC VALUES
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
  @DirtiesContext
  void postNewUnicorn() {
    var garryJson =
        "{\"dateOfBirth\":\"1999-10-12\",\"hornDiameter\":11,\"hornLength\":37,\"maneColor\":\"BLUE\",\"name\":\"Garry\"}";
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(garryJson),
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(response.getHeaders().get("Location")).isNotNull().hasSize(1);

    var anotherResponse =
        restTemplate.getForEntity(
            requireNonNull(response.getHeaders().get("Location")).get(0), String.class);

    assertThat(anotherResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
  }

  @Test
  @DirtiesContext
  void postUnicornWithInvalidHornLengthCausesA400Response() throws JsonProcessingException {
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Larry",
                "maneColor",
                "BLUE",
                "hornLength",
                0,
                "hornDiameter",
                0,
                "dateOfBirth",
                "1999-10-12"));
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(larryJson),
            List.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody())
        .contains("hornLength must be between 1 and 100")
        .contains("hornDiameter must be between 1 and 40");
  }

  @Test
  @DirtiesContext
  void postUnicornWithInvalidHornDiameterCausesA400Response() throws JsonProcessingException {
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Larry",
                "maneColor",
                "BLUE",
                "hornLength",
                101,
                "hornDiameter",
                41,
                "dateOfBirth",
                "1999-10-12"));
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(larryJson),
            List.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody())
        .contains("hornLength must be between 1 and 100")
        .contains("hornDiameter must be between 1 and 40");
  }

  @Test
  @DirtiesContext
  void postUnicornWithInvalidDateOfBirthCausesA400Response() throws JsonProcessingException {
    var larryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Larry",
                "maneColor",
                "BLUE",
                "hornLength",
                37,
                "hornDiameter",
                11,
                "dateOfBirth",
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE)));
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
