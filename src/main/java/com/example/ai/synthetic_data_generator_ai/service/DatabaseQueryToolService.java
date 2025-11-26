package com.example.ai.synthetic_data_generator_ai.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DatabaseQueryToolService
    implements Function<DatabaseQueryToolService.Request, DatabaseQueryToolService.Response> {

  private final DynamicDataService dynamicDataService;

  @Override
  public Response apply(Request request) {

    log.info("SELECT: {}", request.queryStr());

    List<Map<String, Object>> rows = dynamicDataService.executeQuery(request.schemaName(), request.queryStr());
    ResultSetWrapper rsw = new ResultSetWrapper(rows);

    return new Response(rsw);
  }

  public record Request(String schemaName, String queryStr) {
  }

  public record Response(ResultSetWrapper result) {
  }

  public record ResultSetWrapper(List<Map<String, Object>> rows) {
  }

}
