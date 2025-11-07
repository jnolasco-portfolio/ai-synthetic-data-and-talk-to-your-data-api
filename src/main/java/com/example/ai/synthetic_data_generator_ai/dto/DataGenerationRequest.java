package com.example.ai.synthetic_data_generator_ai.dto;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DataGenerationRequest(
        String prompt,
        @Range(min = 0, max = 1) Double temperature,
        @Range(min = 1, max = 100) Integer maxRows,
        String instructions) {
}
