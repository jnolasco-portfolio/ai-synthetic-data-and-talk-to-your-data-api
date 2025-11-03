package com.example.ai.synthetic_data_generator_ai.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.synthetic_data_generator_ai.dto.DataGenerationRequest;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
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

  @PostMapping("/normalize")
  public ResponseEntity<NormalizedSchema> normalizeSchema(
      @NotNull @RequestPart("file") MultipartFile schemaFile,
      @NotBlank @RequestParam("schemaName") String schemaName,
      @NotBlank @RequestParam("dbServer") String dbServer) throws IOException {

    return ResponseEntity
        .ok(schemaAssistantService.normalizeSchema(schemaName, schemaFile.getInputStream(), dbServer));
  }

  @PostMapping("/generate-data")
  public ResponseEntity<byte[]> generateSyntheticData(@RequestBody DataGenerationRequest request) throws IOException {
    ByteArrayOutputStream zipBytes = schemaAssistantService.generateSyntheticData(request.schema(), request.rowCount());

    byte[] payload = zipBytes.toByteArray();
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"synthetic-data.zip\"");
    headers.setContentLength(payload.length);
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

    return ResponseEntity.ok()
        .headers(headers)
        .body(payload);
  }

}
