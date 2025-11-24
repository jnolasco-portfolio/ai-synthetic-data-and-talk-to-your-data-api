package com.example.ai.synthetic_data_generator_ai.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.simple.JdbcClient;

import com.example.ai.synthetic_data_generator_ai.service.DatabaseToolService;

@Configuration
public class FunctionConfiguration {

  @Bean
  @Description("Executes a SQL query against the database and returns the results")
  public Function<DatabaseToolService.Request, DatabaseToolService.Response> databaseToolService(
      JdbcClient jdbcClient) {
    return new DatabaseToolService(jdbcClient);
  }

}
