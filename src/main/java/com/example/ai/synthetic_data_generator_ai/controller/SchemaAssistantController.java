package com.example.ai.synthetic_data_generator_ai.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationResponse;
import com.example.ai.synthetic_data_generator_ai.service.SchemaAssistantService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/schema-assistant")
@RequiredArgsConstructor
public class SchemaAssistantController {

  private final SchemaAssistantService schemaAssistantService;

  @PostMapping("/generate-data")
  public ResponseEntity<DataGenerationResponse> generateSyntheticData(
      @NotBlank @RequestParam("schemaName") String schemaFileName,
      @NotNull @RequestPart("file") MultipartFile schemaFile,
      @NotBlank @RequestPart("parameters") DataGenerationRequest parameters) throws IOException {

    return ResponseEntity
        .ok(schemaAssistantService.generateSyntheticData(schemaFileName, schemaFile.getInputStream(), parameters));
  }

}
