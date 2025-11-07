package com.example.ai.synthetic_data_generator_ai.llm;

import java.io.InputStream;
import java.util.List;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;

public interface LLMSchemaAssistantClient {

  NormalizedSchema normalizeSchema(String conversationId, String schemaName, InputStream schemaStream,
      String userPrompt);

  List<String> getSyntheticDataAsCsv(
      String conversationId,
      NormalizedSchema schema,
      String tableName,
      int rowCount,
      String userInstructions);

}
