package com.example.ai.synthetic_data_generator_ai.dto;

public record DataGenerationRequest(NormalizedSchema schema, int rowCount) {
}