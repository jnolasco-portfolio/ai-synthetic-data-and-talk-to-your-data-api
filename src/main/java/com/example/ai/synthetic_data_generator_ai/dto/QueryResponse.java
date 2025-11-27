package com.example.ai.synthetic_data_generator_ai.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Builder;

@Builder
public record QueryResponse(
        UUID id,
        String conversationId,
        String question,
        String sqlQuery,
        List<Map<String, Object>> result) {
}
