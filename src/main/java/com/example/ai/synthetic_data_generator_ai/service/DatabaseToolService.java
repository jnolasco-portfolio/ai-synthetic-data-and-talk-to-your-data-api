package com.example.ai.synthetic_data_generator_ai.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.jdbc.core.simple.JdbcClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DatabaseToolService implements Function<DatabaseToolService.Request, DatabaseToolService.Response> {

  private final JdbcClient jdbcClient;

  @Override
  public Response apply(Request request) {

    log.info("SELECT: {}", request.queryStr());

    List<Map<String, Object>> rows = jdbcClient.sql(request.queryStr()).query().listOfRows();
    ResultSetWrapper rsw = new ResultSetWrapper(rows);

    return new Response(rsw);
  }

  public record Request(String queryStr) {
  }

  public record Response(ResultSetWrapper result) {
  }

  public record ResultSetWrapper(List<Map<String, Object>> rows) {
  }

}
