package com.example.ai.synthetic_data_generator_ai.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseRequest;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchemaAssistantServiceImpl implements SchemaAssistantService {

        private final LLMSchemaAssistantClient llmSchemaAssistantClient;

        @Override
        public LearnDatabaseResponse learnSchema(
                        @NonNull String conversationId,
                        @NonNull String schemaName,
                        @NonNull InputStream schemaStream,
                        @NonNull LearnDatabaseRequest request) {

                LearnDatabaseResponse normalizeSchema = llmSchemaAssistantClient.learnSchema(conversationId,
                                schemaName,
                                schemaStream,
                                request.prompt());

                return normalizeSchema;
        }

        @Override
        public DataGenerationResponse generateSyntheticData(DataGenerationRequest request) {

                List<String> syntheticData = llmSchemaAssistantClient.getSyntheticDataAsCsv(request.conversationId(),
                                request.schema(),
                                request.tableName(),
                                request.maxRows(),
                                request.instructions());
                return DataGenerationResponse.builder()
                                .tableName(request.tableName())
                                .data(syntheticData)
                                .build();
        }
}
