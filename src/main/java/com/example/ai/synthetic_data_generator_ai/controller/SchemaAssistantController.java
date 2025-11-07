package com.example.ai.synthetic_data_generator_ai.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.service.SchemaAssistantService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Valid
@RestController
@RequestMapping("/api/v1/schema-assistant")
@RequiredArgsConstructor
public class SchemaAssistantController {

  private final SchemaAssistantService schemaAssistantService;

  @PostMapping("/generate-data")
  public ResponseEntity<DataGenerationResponse> generateSyntheticData(
      @NotBlank @RequestParam("conversationId") String conversationId,
      @NotBlank @RequestParam("schemaFileName") String schemaFileName,
      @NotNull @RequestPart("file") MultipartFile file,
      @NotNull @RequestPart("parameters") DataGenerationRequest parameters) throws IOException {

    return ResponseEntity
        .ok(schemaAssistantService.generateSyntheticData(conversationId, schemaFileName, file.getInputStream(),
            parameters));
  }

  @PostMapping("/generate-data/table")
  public ResponseEntity<DataGenerationResponse> generateSyntheticDataByTable(
      @NotBlank @RequestParam("conversationId") String conversationId,
      @NotNull @RequestBody NormalizedSchema schema,
      @NotNull @RequestPart("parameters") DataGenerationRequest parameters,
      @NotBlank @RequestParam("tableName") String tableName) throws IOException {

    return ResponseEntity
        .ok(schemaAssistantService.generateSyntheticData(conversationId, schema, parameters, tableName));
  }

}
