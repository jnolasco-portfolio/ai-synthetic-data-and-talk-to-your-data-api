package com.example.ai.synthetic_data_generator_ai.llm;

import com.example.ai.synthetic_data_generator_ai.dto.QueryResponseMetadata;

public record LLMQueryResponse(String sqlQuery, QueryResponseMetadata metadata) {
}
