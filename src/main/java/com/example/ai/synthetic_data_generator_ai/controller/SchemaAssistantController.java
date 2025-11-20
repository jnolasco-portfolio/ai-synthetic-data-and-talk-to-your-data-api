package com.example.ai.synthetic_data_generator_ai.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseRequest;
import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;
import com.example.ai.synthetic_data_generator_ai.service.SchemaAssistantService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@CrossOrigin()
@Valid
@RestController
@RequestMapping("/api/v1/schema-assistant")
@RequiredArgsConstructor
public class SchemaAssistantController {

  private final SchemaAssistantService schemaAssistantService;

  /**
   * 
   * @param conversationId
   * @param schemaFileName
   * @param file
   * @param parametersJson {"prompt": string, "temperature": number, "maxRows":
   *                       number}
   * @return
   * @throws IOException
   */
  @PostMapping(value = "/learn", consumes = "multipart/form-data")
  public ResponseEntity<LearnDatabaseResponse> learnSchema(
      @NotBlank @RequestParam("conversationId") String conversationId,
      @NotBlank @RequestParam("schemaFileName") String schemaFileName,
      @NotNull @RequestPart("file") MultipartFile file,
      @NotNull @RequestParam("parameters") String parametersJson) throws IOException {

    LearnDatabaseRequest parameters = new com.fasterxml.jackson.databind.ObjectMapper().readValue(parametersJson,
        LearnDatabaseRequest.class);

    return ResponseEntity
        .ok(schemaAssistantService.learnSchema(conversationId, schemaFileName, file.getInputStream(),
            parameters));
  }

  @PostMapping("/generate")
  public ResponseEntity<DataGenerationResponse> generateSyntheticDataByTable(
      @NotNull @RequestBody DataGenerationRequest request) throws IOException {

    return ResponseEntity
        .ok(schemaAssistantService.generateSyntheticData(request));
  }

}
