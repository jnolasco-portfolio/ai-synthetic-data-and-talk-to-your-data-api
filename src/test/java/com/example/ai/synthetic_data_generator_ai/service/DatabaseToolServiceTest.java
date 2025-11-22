package com.example.ai.synthetic_data_generator_ai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:mysql://localhost:3306/library",
    "spring.datasource.username=aiuser",
    "spring.datasource.password=1234#"
})
class DatabaseToolServiceTest {

  @Autowired
  private DatabaseToolService databaseToolService;

  @Test
  void apply_withRealDatabase_shouldReturnAuthorsData() {
    // Arrange
    String query = "select * from Authors";
    DatabaseToolService.Request request = new DatabaseToolService.Request(query);

    // Act
    DatabaseToolService.Response response = databaseToolService.apply(request);

    // Assert
    assertNotNull(response);
    DatabaseToolService.ResultSetWrapper result = response.result();
    assertNotNull(result);
    List<Map<String, Object>> rows = result.rows();
    assertNotNull(rows);
    assertEquals(100, rows.size(), "Expecting 100 rows from the pre-populated Authors table.");

    // Check first row
    Map<String, Object> firstRow = rows.get(0);
    // Note: MySQL column names are case-insensitive by default on some OSes but
    // returned as defined.
    // We will assert against the names defined in the SQL script.
    assertEquals(1, firstRow.get("author_id"));
    assertEquals("Stephen", firstRow.get("first_name"));
    assertEquals(LocalDate.parse("1947-09-21"), ((java.sql.Date) firstRow.get("birth_date")).toLocalDate());
  }
}
