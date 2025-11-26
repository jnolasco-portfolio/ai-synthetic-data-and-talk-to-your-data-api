package com.example.ai.synthetic_data_generator_ai.dto;

import com.google.auto.value.AutoValue.Builder;

import jakarta.validation.constraints.NotBlank;

@Builder
public record QueryRequest(
                @NotBlank String question,
                String conversationId,
                String schemaName) {
}
