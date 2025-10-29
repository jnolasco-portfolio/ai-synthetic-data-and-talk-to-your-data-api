package com.example.ai.synthetic_data_generator_ai.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SchemaAssistantServiceImpl implements SchemaAssistantService {

  private final ChatClient schemaAssistantChatClient;

  @Value("classpath:prompts/convert-schema-to-json.st")
  private Resource normalizeSchemaPrompt;

  @Override
  public NormalizedSchema normalizeSchema(String originalSchema, InputStream schemaStream, String databaseServer) {
    try {
      String schemaString = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);

      NormalizedSchema schema = schemaAssistantChatClient.prompt()
          .user(u -> u.text(normalizeSchemaPrompt)
              .params(Map.of(
                  "database", originalSchema,
                  "schema", schemaString,
                  "database_server", databaseServer)))
          .call()
          .entity(new ParameterizedTypeReference<NormalizedSchema>() {
          });

      return schema;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read schema stream", e);
    }
  }
}
