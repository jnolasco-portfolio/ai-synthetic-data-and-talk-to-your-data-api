package com.example.ai.synthetic_data_generator_ai.dto;

import java.util.List;

public record NormalizedSchema(String database, List<Table> tables) {

  public record Table(
      String name,
      String comment,
      List<Column> columns,
      String primaryKey,
      List<ForeignKey> foreignKeys,
      List<Index> indexes) {

  }

  public record Column(String name,
      String type,
      Boolean nullable,
      String defaultValue,
      Boolean autoIncrement,
      String comment) {
  }

  public record ForeignKey(
      String name,
      List<String> columns,
      String referencedTable,
      List<String> referencedColumns,
      String updateRule,
      String deleteRule) {
  }

  public record Index(
      String name,
      List<String> columns,
      Boolean unique,
      String type) {

  }

}