package com.example.ai.synthetic_data_generator_ai.llm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema.Table;
import com.example.ai.synthetic_data_generator_ai.util.JsonTestUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@TestPropertySource(properties = { "spring.cache.type=simple" })
public class SchemaAssistantClientImplCacheTest {

  @Autowired
  private LLMSchemaAssistantClientImpl underTest;

  @MockitoSpyBean
  private ChatClient chatClient;

  @Value("classpath:data/sample-schema.sql")
  private Resource exampleSchema;
  @Value("classpath:data/sample-schema.json")
  private Resource exampleSchemaJson;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void testNormalizeSchema() throws IOException {

    String conversationId = "987";
    String schemaName = "library";
    String userPrompt = "mysql";

    underTest.normalizeSchema(conversationId, schemaName,
        exampleSchema.getInputStream(), userPrompt);
    NormalizedSchema normalizeSchema = underTest.normalizeSchema(conversationId, schemaName,
        exampleSchema.getInputStream(), userPrompt);

    String json = JsonTestUtils.getObjectAsPrettyJson(normalizeSchema, objectMapper);
    JsonNode actualJson = objectMapper.readTree(json);
    JsonNode expectedJson = objectMapper.readTree(exampleSchemaJson.getInputStream());

    JsonTestUtils.removeFields(actualJson, "comment");
    JsonTestUtils.removeFields(expectedJson, "comment");

    System.out.println(json);

    assertThat(actualJson).isEqualTo(expectedJson);

    verify(chatClient, times(1)).prompt();

  }

  @Test
  void testGetSyntheticDataAsCsv() throws StreamReadException, DatabindException, IOException {

    NormalizedSchema schema = objectMapper.readValue(exampleSchemaJson.getInputStream(), NormalizedSchema.class);
    Table table = schema.getTables().stream()
        .filter(t -> t.getName().equals("Authors"))
        .findFirst()
        .orElseThrow();

    String userInstructions = "Data in spanish language";

    underTest.getSyntheticDataAsCsv("123", schema, table.getName(), 10, userInstructions);
    List<String> rows = underTest.getSyntheticDataAsCsv("123", schema, table.getName(), 10, userInstructions);

    System.out.println(rows);
    assertThat(rows).hasSize(10);
    verify(chatClient, times(1)).prompt();
  }

}
