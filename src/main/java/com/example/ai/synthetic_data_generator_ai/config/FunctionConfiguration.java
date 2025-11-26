package com.example.ai.synthetic_data_generator_ai.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.example.ai.synthetic_data_generator_ai.service.DatabaseMetadataToolService;
import com.example.ai.synthetic_data_generator_ai.service.DatabaseQueryToolService;
import com.example.ai.synthetic_data_generator_ai.service.DynamicDataService;

@Configuration
public class FunctionConfiguration {

  @Bean
  @Description("Executes a SQL query against the database and returns the results")
  public Function<DatabaseQueryToolService.Request, DatabaseQueryToolService.Response> databaseQueryToolService(
      DynamicDataService dynamicDataService) {
    return new DatabaseQueryToolService(dynamicDataService);
  }

  @Bean
  @Description("Extracts the detailed database schema for a given schema name, including tables, columns, primary keys, foreign keys, and indexes")
  public Function<DatabaseMetadataToolService.Request, DatabaseMetadataToolService.Response> databaseMetadataToolService(
      DynamicDataService dynamicDataService) {
    return new DatabaseMetadataToolService(dynamicDataService);
  }
}
