package com.example.ai.synthetic_data_generator_ai.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LearnDatabaseResponse {

  private String server;
  private String schemaName;
  private List<Table> tables;

  @Data
  @NoArgsConstructor
  public static class Table {
    private String name;
    private String comment;
    private List<Column> columns;
    private String primaryKey;
    private List<ForeignKey> foreignKeys;
    private List<Index> indexes;
  }

  @Data
  @NoArgsConstructor
  public static class Column {
    private String name;
    private String type;
    private Boolean nullable;
    private String defaultValue;
    private Boolean autoIncrement;
    private String comment;
  }

  @Data
  @NoArgsConstructor
  public static class ForeignKey {
    private String name;
    private List<String> columns;
    private String referencedTable;
    private List<String> referencedColumns;
    private String updateRule;
    private String deleteRule;
  }

  @Data
  @NoArgsConstructor
  public static class Index {
    private String name;
    private List<String> columns;
    private Boolean unique;
    private String type;
  }
}