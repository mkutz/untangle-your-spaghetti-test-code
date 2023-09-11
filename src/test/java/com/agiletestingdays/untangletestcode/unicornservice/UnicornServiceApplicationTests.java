package com.agiletestingdays.untangletestcode.unicornservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

    var body = objectMapper.readTree(response.getBody());

    assertThat(body).isNotNull();
    assertThat(body.isArray()).isTrue();
    assertThat(body.size()).isEqualTo(1);
  }

  @Test
  void getSingleUnicornWorksAndReturnsData() throws JsonProcessingException {
    var response = restTemplate.getForEntity("%s/unicorns".formatted(baseUrl), String.class);
    var unicornList = objectMapper.readTree(response.getBody());
    var unicornId = unicornList.get(0).get("id").asText();

    var anotherResponse =
        restTemplate.getForEntity("%s/unicorns/%s".formatted(baseUrl, unicornId), String.class);
    var unicornData = objectMapper.readTree(anotherResponse.getBody());

    assertThat(unicornData.has("id")).isTrue();
    assertThat(unicornData.get("id").asText()).isEqualTo(unicornId);

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
  void postNewUnicorn() throws JsonProcessingException {
    var garryJson =
        objectMapper.writeValueAsString(
            Map.of(
                "name",
                "Garry",
                "maneColor",
                "BLUE",
                "hornLength",
                37,
                "hornDiameter",
                11,
                "dateOfBirth",
                "1999-10-12"));
    var response =
        restTemplate.exchange(
            post("%s/unicorns/".formatted(baseUrl))
                .header("Content-Type", "application/json")
                .body(garryJson),
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(response.getHeaders().get("Location")).isNotNull().hasSize(1);

    var anotherResponse =
        restTemplate.getForEntity(response.getHeaders().get("Location").get(0), String.class);

    assertThat(anotherResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
  }
}
