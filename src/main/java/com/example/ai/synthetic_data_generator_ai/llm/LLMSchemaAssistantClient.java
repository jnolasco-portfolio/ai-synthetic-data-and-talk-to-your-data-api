package com.example.ai.synthetic_data_generator_ai.llm;

import java.io.InputStream;
import java.util.List;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;

public interface LLMSchemaAssistantClient {

    LearnDatabaseResponse normalizeSchema(String conversationId, String schemaName, InputStream schemaStream,
            String userPrompt);

    List<String> getSyntheticDataAsCsv(
            String conversationId,
            LearnDatabaseResponse schema,
            String tableName,
            int rowCount,
            String userInstructions);

}
