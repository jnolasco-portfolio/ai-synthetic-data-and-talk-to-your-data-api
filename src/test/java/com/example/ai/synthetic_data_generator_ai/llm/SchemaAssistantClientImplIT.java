package com.example.ai.synthetic_data_generator_ai.llm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse.Table;
import com.example.ai.synthetic_data_generator_ai.util.JsonTestUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class SchemaAssistantClientImplIT {

  @Autowired
  private LLMSchemaAssistantClientImpl underTest;

  @Value("classpath:data/sample-schema.sql")
  private Resource exampleSchema;
  @Value("classpath:data/learn-schema-response.json")
  private Resource exampleSchemaJson;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void testNormalizeSchema() throws IOException {
    LearnDatabaseResponse normalizeSchema = underTest.learnSchema("123", "library", exampleSchema.getInputStream(),
        "mysql");

    String json = JsonTestUtils.getObjectAsPrettyJson(normalizeSchema, objectMapper);
    JsonNode actualJson = objectMapper.readTree(json);
    JsonNode expectedJson = objectMapper.readTree(exampleSchemaJson.getInputStream());

    JsonTestUtils.removeFields(actualJson, "comment");
    JsonTestUtils.removeFields(expectedJson, "comment");

    System.out.println(json);

    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  void testGetSyntheticDataAsCsv() throws StreamReadException, DatabindException, IOException {

    LearnDatabaseResponse schema = objectMapper.readValue(exampleSchemaJson.getInputStream(),
        LearnDatabaseResponse.class);
    Table table = schema.getTables().stream()
        .filter(t -> t.getName().equals("Authors"))
        .findFirst()
        .orElseThrow();

    String userInstructions = "Data in spanish language";

    List<String> rows = underTest.getSyntheticDataAsCsv("123", schema, table.getName(), 10, userInstructions);
    System.out.println(rows);
    assertThat(rows).hasSize(10);
  }

  @Test
  void testQueryShowMeAuthors() throws Exception, DatabindException, IOException {

    LearnDatabaseResponse schema = objectMapper.readValue(exampleSchemaJson.getInputStream(),
        LearnDatabaseResponse.class);

    String question = "Show me authors";
    String sqlQuery = underTest.generateSqlQuery("123", schema, question);
    log.info("SQL Query: {}", sqlQuery);
    assertThat(sqlQuery).isEqualTo("SELECT * FROM Authors");

  }

  @Test
  void testQueryHowManyBooks() throws Exception, DatabindException, IOException {

    LearnDatabaseResponse schema = objectMapper.readValue(exampleSchemaJson.getInputStream(),
        LearnDatabaseResponse.class);

    String question = "How many books?";
    String sqlQuery = underTest.generateSqlQuery("123", schema, question);
    log.info("SQL Query: {}", sqlQuery);
    assertThat(sqlQuery).isEqualToIgnoringCase("SELECT count(*) FROM Books");
  }

  @Test
  void testQueryShowMeHowManyBooksByAuthor() throws Exception, DatabindException, IOException {

    LearnDatabaseResponse schema = objectMapper.readValue(exampleSchemaJson.getInputStream(),
        LearnDatabaseResponse.class);

    String question = "How many books by author?";
    String sqlQuery = underTest.generateSqlQuery("123", schema, question);
    log.info("SQL Query: {}", sqlQuery);

    String expectedSqlQuery = """
        SELECT
          A.first_name,
          A.last_name,
          COUNT(B.book_id) AS number_of_books
        FROM Authors AS A
        JOIN Books AS B
          ON A.author_id = B.author_id
        GROUP BY
          A.author_id,
          A.first_name,
          A.last_name
        ORDER BY
          number_of_books DESC;
                """;
    assertThat(sqlQuery.trim()).isEqualToIgnoringCase(expectedSqlQuery.trim());
  }

  @Test
  void testQueryShowMeHowManyBooksAreLended() throws Exception, DatabindException, IOException {

    LearnDatabaseResponse schema = objectMapper.readValue(exampleSchemaJson.getInputStream(),
        LearnDatabaseResponse.class);

    String question = "How many books by author?";
    String sqlQuery = underTest.generateSqlQuery("123", schema, question);
    log.info("SQL Query: {}", sqlQuery);

    String expectedSqlQuery = """
        SELECT
          A.first_name,
          A.last_name,
          COUNT(B.book_id) AS number_of_books
        FROM Authors AS A
        JOIN Books AS B
          ON A.author_id = B.author_id
        GROUP BY
          A.author_id,
          A.first_name,
          A.last_name
        ORDER BY
          number_of_books DESC;
                """;
    assertThat(sqlQuery.trim()).isEqualToIgnoringCase(expectedSqlQuery.trim());
  }

}
