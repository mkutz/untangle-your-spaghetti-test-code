package com.agiletestingdays.untangletestcode.unicornservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootApplication
@Testcontainers
class LocalTestApplication {

  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgreSQLContainer() {
    return new PostgreSQLContainer<>("postgres:16");
  }

  public static void main(String[] args) {
    SpringApplication.from(Application::main).run(args);
  }
}
