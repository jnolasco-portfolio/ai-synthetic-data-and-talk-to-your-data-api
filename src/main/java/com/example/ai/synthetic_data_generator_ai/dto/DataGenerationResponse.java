package com.example.ai.synthetic_data_generator_ai.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record DataGenerationResponse(String tableName, List<String> data) {
}