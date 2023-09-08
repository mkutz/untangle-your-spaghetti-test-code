package com.agiletestingdays.untangletestcode.unicornservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
public class UnicornServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UnicornServiceApplication.class, args);
  }
}
