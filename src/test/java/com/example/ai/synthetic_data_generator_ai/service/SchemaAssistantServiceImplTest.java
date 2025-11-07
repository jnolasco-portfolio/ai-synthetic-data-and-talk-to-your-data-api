package com.example.ai.synthetic_data_generator_ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class SchemaAssistantServiceImplTest {

  @Value("classpath:data/sample-schema.json")
  private Resource exampleSchemaJson;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SchemaAssistantService underTest;

  @Test
  void testGenerateSyntheticData() throws StreamReadException, DatabindException, IOException {

    NormalizedSchema schema = objectMapper.readValue(exampleSchemaJson.getInputStream(), NormalizedSchema.class);

    assertThatNoException().isThrownBy(() -> {
      DataGenerationResponse response = underTest
          .generateSyntheticData("123", "library", exampleSchemaJson.getInputStream(), builDataGenerationRequest());

      assertThat(response).isNotNull();
      assertThat(response.schema()).isNotNull();
      assertThat(response.data()).isNotNull();

    });
  }

  private DataGenerationRequest builDataGenerationRequest() {
    return DataGenerationRequest.builder()
        .maxRows(20)
        .temperature(0.7)
        .prompt("Data in spanish language")
        .instructions("Keep track of primary keys generated in parent tables in order to use them in child tables")
        .build();
  }
}
