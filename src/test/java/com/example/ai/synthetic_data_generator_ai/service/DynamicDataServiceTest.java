package com.example.ai.synthetic_data_generator_ai.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "app.datasource.library.jdbc-url=jdbc:mysql://localhost:3306/library",
    "app.datasource.library.username=aiuser",
    "app.datasource.library.password=1234#"
})
public class DynamicDataServiceTest {

  @Autowired
  private DynamicDataService dynamicDataService;

  @Test
  void testExecuteQuery() {
    // TODO: Implement test
  }

  @Test
  void testGetDatabaseSchema() {
    // Act
    LearnDatabaseResponse response = dynamicDataService.getDatabaseSchema("library");

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.getDatabase()).isEqualTo("library");
    assertThat(response.getTables()).isNotEmpty();

    // Assertions for a specific table, e.g., "Authors"
    Optional<LearnDatabaseResponse.Table> authorsTableOpt = response.getTables().stream()
        .filter(t -> "Authors".equalsIgnoreCase(t.getName())).findFirst();
    assertThat(authorsTableOpt).isPresent();
    LearnDatabaseResponse.Table authorsTable = authorsTableOpt.get();
    assertThat(authorsTable.getPrimaryKey()).isEqualTo("author_id");
    assertThat(authorsTable.getColumns()).anyMatch(c -> "first_name".equals(c.getName()));
  }
}
