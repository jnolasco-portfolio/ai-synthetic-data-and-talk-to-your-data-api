package com.example.ai.synthetic_data_generator_ai.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;

public interface SchemaAssistantService {

  ByteArrayOutputStream generateSyntheticData(NormalizedSchema schema, int rowCount) throws IOException;

  NormalizedSchema normalizeSchema(String schemaName, InputStream schemaStream, String databaseServer);

}
