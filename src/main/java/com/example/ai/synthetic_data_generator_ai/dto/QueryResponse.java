package com.example.ai.synthetic_data_generator_ai.dto;

import java.util.List;
import java.util.Map;

import com.google.auto.value.AutoValue.Builder;

@Builder
public record QueryResponse(
    String conversationId,
    String question,
    String sqlQuery,
    List<Map<String, Object>> result) {
}
