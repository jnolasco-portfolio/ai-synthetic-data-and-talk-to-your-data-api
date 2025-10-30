package com.example.ai.synthetic_data_generator_ai.llm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;

public interface LLMSchemaAssistantClient {

  NormalizedSchema normalizeSchema(String originalSchema, InputStream schemaStream, String databaseServer);

  List<String> getSyntheticDataAsCsv(NormalizedSchema.Table table, int rowCount);

}
