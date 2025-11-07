package com.example.ai.synthetic_data_generator_ai.dto;

import lombok.Builder;

@Builder
public record DataGenerationRequest(String prompt, Double temperature, int maxRows, String instructions) {
}