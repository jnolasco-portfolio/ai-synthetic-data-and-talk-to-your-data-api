package com.example.ai.synthetic_data_generator_ai.dto;

import lombok.Builder;

@Builder
public record LearnSchemaResponse(NormalizedSchema schema) {
}