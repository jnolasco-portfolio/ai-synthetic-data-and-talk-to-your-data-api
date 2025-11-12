package com.example.ai.synthetic_data_generator_ai.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.LearnSchemaRequest;
import com.example.ai.synthetic_data_generator_ai.dto.LearnSchemaResponse;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchemaAssistantServiceImpl implements SchemaAssistantService {

        private final LLMSchemaAssistantClient llmSchemaAssistantClient;

        @Override
        public LearnSchemaResponse learnSchema(
                        @NonNull String conversationId,
                        @NonNull String schemaName,
                        @NonNull InputStream schemaStream,
                        @NonNull LearnSchemaRequest request) {

                NormalizedSchema normalizeSchema = llmSchemaAssistantClient.normalizeSchema(conversationId, schemaName,
                                schemaStream,
                                request.prompt());

                return LearnSchemaResponse.builder()
                                .schema(normalizeSchema)
                                .build();
        }

        @Override
        public DataGenerationResponse generateSyntheticData(
                        String conversationId,
                        NormalizedSchema normalizeSchema,
                        DataGenerationRequest request,
                        String tableName) {

                List<String> syntheticData = llmSchemaAssistantClient.getSyntheticDataAsCsv(conversationId,
                                normalizeSchema,
                                tableName,
                                request.maxRows(),
                                request.instructions());
                return DataGenerationResponse.builder()
                                .data(syntheticData)
                                .build();
        }
}
