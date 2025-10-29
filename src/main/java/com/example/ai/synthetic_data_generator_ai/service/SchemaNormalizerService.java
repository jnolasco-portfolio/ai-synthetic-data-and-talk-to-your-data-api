package com.example.ai.synthetic_data_generator_ai.service;

import java.io.InputStream;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;

public interface SchemaNormalizerService {

  NormalizedSchema normalizeSchema(String originalSchema, InputStream schemaStream, String databaseServer);

}
