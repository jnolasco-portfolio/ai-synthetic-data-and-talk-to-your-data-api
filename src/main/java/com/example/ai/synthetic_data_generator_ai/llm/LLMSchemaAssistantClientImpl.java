package com.example.ai.synthetic_data_generator_ai.llm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LLMSchemaAssistantClientImpl implements LLMSchemaAssistantClient {

  private final ChatClient schemaAssistantChatClient;

  @Value("classpath:prompts/convert-schema-to-json.st")
  private Resource normalizeSchemaPrompt;

  @Value("classpath:prompts/generate-synthetic-data.st")
  private Resource generateSyntheticDataPrompt;

  @Override
  @Cacheable(cacheNames = "llmCache", key = "{#conversationId, #schemaName, #userPrompt}")
  public LearnDatabaseResponse normalizeSchema(
      String conversationId,
      String schemaName,
      InputStream schemaStream,
      String userPrompt) {

    log.info("Normalizing schema for conversationId: {}, schemaName: {}", conversationId, schemaName);

    if (schemaName == null) {
      throw new IllegalArgumentException("Schema name cannot be null");
    }

    try {
      String ddl = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);
      log.debug("Schema DDL: {}", ddl);

      // TODO: Temperature
      LearnDatabaseResponse schema = schemaAssistantChatClient
          .prompt()
          .user(u -> u.text(normalizeSchemaPrompt)
              .params(Map.of(
                  "database", schemaName,
                  "schema", ddl,
                  "user_prompt", userPrompt)))
          .call()
          .entity(new ParameterizedTypeReference<LearnDatabaseResponse>() {
          });

      log.info("Successfully normalized schema for conversationId: {}", conversationId);
      log.debug("Normalized schema: {}", schema);
      return schema;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read schema stream for conversationId: " + conversationId, e);
    }
  }

  @Override
  @Cacheable(cacheNames = "csvCache", key = "{#conversationId, #schema, #tableName, #rowCount, #userInstructions}")
  public List<String> getSyntheticDataAsCsv(
      String conversationId,
      @NonNull LearnDatabaseResponse schema,
      @NonNull String tableName,
      int rowCount,
      @NonNull String userInstructions) {
    log.info("Generating synthetic data for conversationId: {}, tableName: {}, rowCount: {}", conversationId, tableName,
        rowCount);

    // TODO: Add temperature from user
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

    log.info("Successfully generated {} CSV rows for conversationId: {}", csvRows.size(), conversationId);
    return csvRows;
  }

}
