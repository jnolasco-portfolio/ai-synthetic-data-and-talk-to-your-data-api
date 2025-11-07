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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchemaAssistantServiceImpl implements SchemaAssistantService {

  private final LLMSchemaAssistantClient llmSchemaAssistantClient;

  @Override
  public DataGenerationResponse generateSyntheticData(
      String schemaName,
      InputStream schemaStream,
      DataGenerationRequest request) {

    NormalizedSchema normalizeSchema = llmSchemaAssistantClient.normalizeSchema(schemaName, schemaStream,
        request.prompt());

    Map<String, List<String>> syntheticData = new HashMap<>();

    normalizeSchema.getTables().stream()
        .forEach(table -> {

          syntheticData.put(table.getName(),
              llmSchemaAssistantClient.getSyntheticDataAsCsv(normalizeSchema, table.getName(), request.maxRows(),
                  request.instructions()));

        });

    return DataGenerationResponse.builder()
        .schema(normalizeSchema)
        .data(syntheticData)
        .build();
  }

  @Override
  public DataGenerationResponse generateSyntheticData(NormalizedSchema schema, DataGenerationRequest request,
      String tableName) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'generateSyntheticData'");
  }
}
