package com.github.zesetup.vaadin8grid;

import javax.inject.Inject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.github.zesetup.vaadin8grid.data.EmployeeRepository;
import com.github.zesetup.vaadin8grid.domain.Employee;

/**
 * Application class.
 * @author Maksim Paz
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.github.zesetup.vaadin8grid"})
public class Application extends SpringBootServletInitializer {
  @Inject 
  EmployeeRepository employeeRepository;
  
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public Application() {
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

  public static void main(String[] args)  throws Exception {
    SpringApplication.run(Application.class, args);
  }

  /**
   *
   * @return lambda.
   */
  @Bean
  public CommandLineRunner  loadData() {
    return (args) -> {
      for (int i = 0;i < 30000;i++) {
        employeeRepository.save(new Employee("login" + i, "name_" + i, "surname_" + i, "position_"
            + i));
      }
    };
  }
}
