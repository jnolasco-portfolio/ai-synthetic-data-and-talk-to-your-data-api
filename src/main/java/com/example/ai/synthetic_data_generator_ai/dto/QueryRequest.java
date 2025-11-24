package com.example.ai.synthetic_data_generator_ai.dto;

import jakarta.validation.constraints.NotBlank;

public record QueryRequest(
        @NotBlank String question,
        String conversationId,
        LearnDatabaseResponse schema) {
}
