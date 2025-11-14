package com.example.ai.synthetic_data_generator_ai.service;

import java.io.InputStream;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseRequest;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;

public interface SchemaAssistantService {

        LearnDatabaseResponse learnSchema(String conversationId, String schemaName, InputStream schemaStream,
                        LearnDatabaseRequest request);

        DataGenerationResponse generateSyntheticData(DataGenerationRequest request);

}
