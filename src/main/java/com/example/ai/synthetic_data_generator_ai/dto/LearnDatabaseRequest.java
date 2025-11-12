package com.example.ai.synthetic_data_generator_ai.dto;

import org.hibernate.validator.constraints.Range;

import lombok.Builder;

@Builder
public record LearnDatabaseRequest(
    String prompt,
    @Range(min = 0, max = 1) Double temperature,
    @Range(min = 1, max = 100) Integer maxRows) {
}
