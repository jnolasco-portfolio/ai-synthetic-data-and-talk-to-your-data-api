package com.example.ai.synthetic_data_generator_ai.llm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LLMSchemaAssistantClientImpl implements LLMSchemaAssistantClient {

  private final ChatClient schemaAssistantChatClient;

  @Value("classpath:prompts/convert-schema-to-json.st")
  private Resource normalizeSchemaPrompt;

  @Value("classpath:prompts/generate-synthetic-data.st")
  private Resource generateSyntheticDataPrompt;

  @Override
  public NormalizedSchema normalizeSchema(String schemaName, InputStream schemaStream, String userPrompt) {

    if (schemaName == null)
      throw new IllegalArgumentException("Schema name cannot be null");

    try {
      String ddl = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);

      NormalizedSchema schema = schemaAssistantChatClient.prompt()
          .user(u -> u.text(normalizeSchemaPrompt)
              .params(Map.of(
                  "database", schemaName,
                  "schema", ddl,
                  "user_prompt", userPrompt)))
          .call()
          .entity(new ParameterizedTypeReference<NormalizedSchema>() {
          });

      return schema;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read schema stream", e);
    }
  }

  @Override
  public List<String> getSyntheticDataAsCsv(@NonNull NormalizedSchema schema, @NonNull String tableName,
      @NonNull int rowCount,
      @NonNull String userInstructions) {

    List<String> csvRows = schemaAssistantChatClient.prompt()
        .user(u -> u.text(generateSyntheticDataPrompt)
            .params(Map.of(
                "schema", schema,
                "tableName", tableName,
                "rowCount", rowCount,
                "user_instructions", userInstructions)))
        .call()
        .entity(new ParameterizedTypeReference<List<String>>() {
        });

    return csvRows;
  }

}
