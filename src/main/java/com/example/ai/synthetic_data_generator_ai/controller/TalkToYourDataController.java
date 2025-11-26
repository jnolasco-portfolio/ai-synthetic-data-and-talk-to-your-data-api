package com.example.ai.synthetic_data_generator_ai.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai.synthetic_data_generator_ai.dto.QueryRequest;
import com.example.ai.synthetic_data_generator_ai.dto.QueryResponse;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;
import com.example.ai.synthetic_data_generator_ai.service.DynamicDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/query")
public class TalkToYourDataController {

    private static final String I_CANNOT_ANSWER_THIS_QUESTION_WITH_THE_AVAILABLE_DATA = "I cannot answer this question with the available data.";
    private final DynamicDataService dynamicDataService;
    private final LLMSchemaAssistantClient llmClient;

    @PostMapping
    public QueryResponse query(@RequestBody QueryRequest request) {

        String sql = llmClient.generateSqlQuery(request.conversationId(), request.schema(), request.question());
        log.info("Talking to your data. conversationId: {}, question: {}, SQLQuery: {}", request.conversationId(),
                request.question());

        if (sql.equalsIgnoreCase(I_CANNOT_ANSWER_THIS_QUESTION_WITH_THE_AVAILABLE_DATA)) {
            return new QueryResponse(request.conversationId(), request.question(), "N/A",
                    List.of(Map.of("Error", I_CANNOT_ANSWER_THIS_QUESTION_WITH_THE_AVAILABLE_DATA)));
        }

        List<Map<String, Object>> result = dynamicDataService.executeQuery(request.schema().getDatabase(), sql);

        return new QueryResponse(request.conversationId(), request.question(), sql, result);
    }
}
