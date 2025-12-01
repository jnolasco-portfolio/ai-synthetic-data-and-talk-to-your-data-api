package com.example.ai.synthetic_data_generator_ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QueryResponseMetadata(
    @JsonProperty("content_type") String contentType,
    @JsonProperty("category_key") String categoryKey,
    @JsonProperty("value_key") String valueKey) {
}
