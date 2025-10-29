package com.example.ai.synthetic_data_generator_ai.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.util.JsonTestUtils;
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
}
