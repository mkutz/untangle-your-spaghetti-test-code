package com.agiletestingdays.untangletestcode.unicornservice;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@EnableJdbcRepositories
public class Application {

  static {
    Locale.setDefault(Locale.ENGLISH);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    return new LocalValidatorFactoryBean();
  }
}
