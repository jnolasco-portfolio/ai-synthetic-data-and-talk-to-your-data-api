package com.example.ai.synthetic_data_generator_ai.controller;

import com.example.ai.synthetic_data_generator_ai.service.DynamicDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai.synthetic_data_generator_ai.dto.QueryRequest;
import com.example.ai.synthetic_data_generator_ai.dto.QueryResponse;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TalkToYourDataController {

    private final DynamicDataService dynamicDataService;
    private final LLMSchemaAssistantClient llmClient;

    /**
     * An example endpoint to test dynamic datasource selection.
     * Call this with /data/company, /data/library, or /data/restaurant.
     * 
     * @param schemaName The name of the schema to connect to.
     * @return A message indicating the result of the connection attempt.
     */
    @GetMapping("/data/{schemaName}")
    public String getDatabaseName(@PathVariable String schemaName) {
        return dynamicDataService.getDatabaseName(schemaName);
    }

    @PostMapping("/query")
    public QueryResponse query(@RequestBody QueryRequest request) {
        String sql = llmClient.generateSqlQuery(request.conversationId(), request.schema(), request.question());
        List<Map<String, Object>> result = dynamicDataService.executeQuery(request.schema().getDatabase(), sql);

        return new QueryResponse(request.conversationId(), request.question(), sql, result);
    }
}
