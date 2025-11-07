package com.example.ai.synthetic_data_generator_ai.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchemaAssistantServiceImpl implements SchemaAssistantService {

  private final LLMSchemaAssistantClient llmSchemaAssistantClient;

  @Override
  public DataGenerationResponse generateSyntheticData(
      @NonNull String conversationId,
      @NonNull String schemaName,
      @NonNull InputStream schemaStream,
      @NonNull DataGenerationRequest request) {

    NormalizedSchema normalizeSchema = llmSchemaAssistantClient.normalizeSchema(conversationId, schemaName,
        schemaStream,
        request.prompt());

    Map<String, List<String>> syntheticData = new HashMap<>();

    normalizeSchema.getTables().stream()
        .forEach(table -> {

          syntheticData.put(table.getName(),
              llmSchemaAssistantClient.getSyntheticDataAsCsv(conversationId, normalizeSchema, table.getName(),
                  request.maxRows(),
                  request.instructions()));

        });

    return DataGenerationResponse.builder()
        .schema(normalizeSchema)
        .data(syntheticData)
        .build();
  }

  @Override
  public DataGenerationResponse generateSyntheticData(
      String conversationId,
      NormalizedSchema schema,
      DataGenerationRequest request,
      String tableName) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'generateSyntheticData'");
  }
}
