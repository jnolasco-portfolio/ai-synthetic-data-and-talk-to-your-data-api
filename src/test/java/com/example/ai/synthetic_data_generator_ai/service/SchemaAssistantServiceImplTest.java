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
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseRequest;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;
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
  void testLearnSchema() throws StreamReadException, DatabindException, IOException {

    assertThatNoException().isThrownBy(() -> {
      LearnDatabaseResponse response = underTest
          .learnSchema("123", "library", exampleSchemaJson.getInputStream(), builLearnSchemaRequest());

      assertThat(response).isNotNull();
      assertThat(response.getTables()).isNotEmpty();

    });
  }

  @Test
  void testGenerateSyntheticDataByTable() throws StreamReadException, DatabindException, IOException {

    assertThatNoException().isThrownBy(() -> {
      DataGenerationResponse response = underTest
          .generateSyntheticData(builDataGenerationRequest());

      assertThat(response).isNotNull();
      assertThat(response.data()).isNotNull();

    });
  }

  private LearnDatabaseRequest builLearnSchemaRequest() {
    return LearnDatabaseRequest.builder()
        .maxRows(20)
        .temperature(0.2)
        .prompt("Data in spanish language")
        .build();
  }

  private DataGenerationRequest builDataGenerationRequest() throws StreamReadException, DatabindException, IOException {
    LearnDatabaseResponse schema = objectMapper.readValue(exampleSchemaJson.getInputStream(),
        LearnDatabaseResponse.class);
    return DataGenerationRequest.builder()
        .tableName("Authors")
        .conversationId("123")
        .schema(schema)
        .maxRows(20)
        .temperature(0.2)
        .instructions("Keep track of primary keys generated in parent tables in order to use them in child tables")
        .build();
  }
}
