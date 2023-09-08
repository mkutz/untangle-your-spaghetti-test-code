package com.agiletestingdays.untangletestcode.unicornservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UnicornServiceApplicationTests {

  @Autowired ApplicationContext applicationContext;
  @LocalServerPort int port;
  @Autowired TestRestTemplate restTemplate;

  @Test
  void contextLoads() {
    assertThat(applicationContext).isNotNull();
  }

  @Test
  void getUnicornsWorksAndReturnsNonEmptyList() {
    ResponseEntity<String> response =
        restTemplate.getForEntity("http://localhost:" + port + "/unicorns", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(response.getBody()).contains("Grace");
  }
}
