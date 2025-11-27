package com.example.ai.synthetic_data_generator_ai.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;
import com.example.ai.synthetic_data_generator_ai.dto.QueryRequest;
import com.example.ai.synthetic_data_generator_ai.dto.QueryResponse;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;
import com.example.ai.synthetic_data_generator_ai.service.DynamicDataService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin()
@RestController
@Valid
@RequiredArgsConstructor
@RequestMapping("/api/v1/schema-assistant")
public class TalkToYourDataController {

    private static final String I_CANNOT_ANSWER_THIS_QUESTION_WITH_THE_AVAILABLE_DATA = "I cannot answer this question with the available data.";
    private final DynamicDataService dynamicDataService;
    private final LLMSchemaAssistantClient llmClient;

    @PostMapping("questions")
    public ResponseEntity<QueryResponse> query(@RequestBody QueryRequest request) {

        LearnDatabaseResponse databaseSchema = dynamicDataService
                .getDatabaseSchema(request.schemaName());

        String sql = llmClient.generateSqlQuery(request.conversationId(), databaseSchema, request.question());
        log.info("Talking to your data. conversationId: {}, question: {}, SQLQuery: {}", request.conversationId(),
                request.question());

        if (sql.equalsIgnoreCase(I_CANNOT_ANSWER_THIS_QUESTION_WITH_THE_AVAILABLE_DATA)) {
            return ResponseEntity
                    .ok(new QueryResponse(UUID.randomUUID(), request.conversationId(), request.question(), "N/A",
                            List.of(Map.of("Error", I_CANNOT_ANSWER_THIS_QUESTION_WITH_THE_AVAILABLE_DATA))));
        }

        List<Map<String, Object>> result = dynamicDataService.executeQuery(request.schemaName(), sql);

        return ResponseEntity
                .ok(new QueryResponse(UUID.randomUUID(), request.conversationId(), request.question(), sql, result));
    }
}
