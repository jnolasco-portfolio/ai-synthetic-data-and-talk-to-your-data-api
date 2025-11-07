package com.example.ai.synthetic_data_generator_ai.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;

@Builder
public record DataGenerationResponse(NormalizedSchema schema, Map<String, List<String>> data) {
}