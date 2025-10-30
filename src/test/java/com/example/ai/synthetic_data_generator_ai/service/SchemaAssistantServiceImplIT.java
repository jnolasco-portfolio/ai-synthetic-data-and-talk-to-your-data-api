package com.example.ai.synthetic_data_generator_ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema.Table;
import com.example.ai.synthetic_data_generator_ai.util.JsonTestUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class SchemaAssistantServiceImplIT {

  @Autowired
  private SchemaAssistantServiceImpl underTest;

  @Value("classpath:data/sample-schema.sql")
  private Resource exampleSchema;
  @Value("classpath:data/sample-schema.json")
  private Resource exampleSchemaJson;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void testNormalizeSchema() throws IOException {
    NormalizedSchema normalizeSchema = underTest.normalizeSchema("library", exampleSchema.getInputStream(), "mysql");
    String json = objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(normalizeSchema);

    JsonNode actualJson = objectMapper.readTree(json);
    JsonNode expectedJson = objectMapper.readTree(exampleSchemaJson.getInputStream());

    JsonTestUtils.removeFields(actualJson, "comment");
    JsonTestUtils.removeFields(expectedJson, "comment");

    System.out.println(json);

    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  void testGetSyntheticDataAsCsv() throws StreamReadException, DatabindException, IOException {

    NormalizedSchema schema = objectMapper.readValue(exampleSchemaJson.getInputStream(), NormalizedSchema.class);
    Table table = schema.tables().stream()
        .filter(t -> t.name().equals("Authors"))
        .findFirst()
        .orElseThrow();

    List<String> rows = underTest.getSyntheticDataAsCsv(table, 10);
    assertThat(rows).hasSize(10);
  }

  @Test
  void testGenerateSyntheticData() throws StreamReadException, DatabindException, IOException {

    NormalizedSchema schema = objectMapper.readValue(exampleSchemaJson.getInputStream(), NormalizedSchema.class);

    assertThatNoException().isThrownBy(() -> {
      byte[] payload = underTest.generateSyntheticData(schema, 10).toByteArray();
      assertThat(payload).isNotEmpty();
    });
  }
}
