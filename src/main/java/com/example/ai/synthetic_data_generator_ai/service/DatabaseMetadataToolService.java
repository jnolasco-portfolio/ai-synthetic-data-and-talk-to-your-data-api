package com.example.ai.synthetic_data_generator_ai.service;

import java.util.function.Function;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DatabaseMetadataToolService
    implements Function<DatabaseMetadataToolService.Request, DatabaseMetadataToolService.Response> {

  private final DynamicDataService dynamicDataService;

  public record Request(String schemaName) {
  }

  public record Response(LearnDatabaseResponse schema) {
  }

  @Override
  public Response apply(Request request) {
    LearnDatabaseResponse databaseSchema = dynamicDataService.getDatabaseSchema(request.schemaName);
    return new Response(databaseSchema);
  }

}
