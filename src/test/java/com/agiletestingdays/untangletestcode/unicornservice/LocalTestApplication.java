package com.agiletestingdays.untangletestcode.unicornservice;

import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootApplication
@Testcontainers
class LocalTestApplication {

  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgreSQLContainer() {
    return new PostgreSQLContainer<>("postgres:16")
        // see
        // https://github.com/rancher-sandbox/rancher-desktop/issues/2609#issuecomment-1788871956
        .waitingFor(
            new WaitAllStrategy()
                .withStrategy(Wait.forListeningPort())
                .withStrategy(
                    Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 2)
                        .withStartupTimeout(Duration.ofSeconds(60))));
  }

  public static void main(String[] args) {
    SpringApplication.from(Application::main).run(args);
  }
}
