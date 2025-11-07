package com.example.ai.synthetic_data_generator_ai.service;

import java.io.InputStream;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;

public interface SchemaAssistantService {

  DataGenerationResponse generateSyntheticData(String schemaName, InputStream schemaStream,
      DataGenerationRequest request);

  DataGenerationResponse generateSyntheticData(NormalizedSchema schema,
      DataGenerationRequest request,
      String tableName);

}
