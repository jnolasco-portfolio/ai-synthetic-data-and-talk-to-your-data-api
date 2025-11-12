package com.example.ai.synthetic_data_generator_ai.service;

import java.io.InputStream;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.LearnSchemaRequest;
import com.example.ai.synthetic_data_generator_ai.dto.LearnSchemaResponse;

public interface SchemaAssistantService {

        LearnSchemaResponse learnSchema(String conversationId, String schemaName, InputStream schemaStream,
                        LearnSchemaRequest request);

        DataGenerationResponse generateSyntheticData(DataGenerationRequest request);

}
