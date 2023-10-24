package com.agiletestingdays.untangletestcode.unicornservice;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.RequestEntity.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UnicornServiceApplicationTests {

  @Value("http://localhost:${local.server.port}")
  String baseUrl;

  @Autowired TestRestTemplate restTemplate;
  @Autowired TestDataManager testDataManager;
  UnicornTestDataBuilder unicornTestDataBuilder = new UnicornTestDataBuilder();
  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void resetDatabase() {
    testDataManager.clear();
  }

  @Test
  void get_all() throws JsonProcessingException {
    // ARRANGE
    testDataManager.withUnicorn(unicornTestDataBuilder.build());

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
  void get_all_empty() throws JsonProcessingException {
    // ARRANGE
    testDataManager.clear();

    // ACT
    var response = restTemplate.getForEntity("%s/unicorns".formatted(baseUrl), String.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    var body = objectMapper.readTree(response.getBody());

    assertThat(body).isNotNull();
    assertThat(body.isArray()).isTrue();
    assertThat(body.size()).isZero();
  }

  @Test
  void get_single() throws JsonProcessingException {
    // ARRANGE
    var unicorn = unicornTestDataBuilder.build();
    testDataManager.withUnicorn(unicorn);

    // ACT
    var response =
        restTemplate.getForEntity("%s/unicorns/%s".formatted(baseUrl, unicorn.id()), String.class);

    // ASSERT
    var unicornData = objectMapper.readTree(response.getBody());

    assertSoftly(
        softly -> {
          softly.assertThat(unicornData.get("id").asText()).isEqualTo(unicorn.id().toString());
          softly.assertThat(unicornData.get("name").asText()).isEqualTo(unicorn.name());
          softly
              .assertThat(unicornData.get("maneColor").asText())
              .isEqualTo(unicorn.maneColor().name());
          softly.assertThat(unicornData.get("hornLength").asInt()).isEqualTo(unicorn.hornLength());
          softly
              .assertThat(unicornData.get("hornDiameter").asInt())
              .isEqualTo(unicorn.hornDiameter());
          softly
              .assertThat(unicornData.get("dateOfBirth").asText())
              .isEqualTo(unicorn.dateOfBirth().format(DateTimeFormatter.ISO_DATE));
        });
  }

  @Test
  void post_new() {
    // ACT
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(
                    "{\"dateOfBirth\":\"1999-10-12\",\"hornDiameter\":11,\"hornLength\":37,\"maneColor\":\"BLUE\",\"name\":\"Garry\"}"),
            String.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(response.getHeaders().get("Location")).isNotNull().hasSize(1);
  }

  @Test
  void get_location_header() {
    // ARRANGE
    var postResponse =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(
                    "{\"dateOfBirth\":\"1999-10-12\",\"hornDiameter\":11,\"hornLength\":37,\"maneColor\":\"BLUE\",\"name\":\"Garry\"}"),
            String.class);

    assumeThat(postResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assumeThat(postResponse.getHeaders().get("Location")).isNotNull().hasSize(1);

    // ACT
    var response =
        restTemplate.getForEntity(
            requireNonNull(postResponse.getHeaders().get("Location")).get(0), String.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
  }

  @Test
  void post_invalid() {
    // ACT
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(
                    "{\"dateOfBirth\":\"1999-10-12\",\"name\":\"Larry\",\"maneColor\":\"BLUE\",\"hornLength\":0,\"hornDiameter\":0}"),
            List.class);

    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    assertThat(response.getHeaders().containsKey("Location")).isFalse();
    assertThat(response.getBody())
        .contains("hornLength must be between 1 and 100")
        .contains("hornDiameter must be between 1 and 40");
  }
}
