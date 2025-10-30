package com.example.ai.synthetic_data_generator_ai.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema.Table;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SchemaAssistantServiceImpl implements SchemaAssistantService {

  private final ChatClient schemaAssistantChatClient;

  @Value("classpath:prompts/convert-schema-to-json.st")
  private Resource normalizeSchemaPrompt;

  @Value("classpath:prompts/generate-synthetic-data.st")
  private Resource generateSyntheticDataPrompt;

  @Override
  public NormalizedSchema normalizeSchema(String originalSchema, InputStream schemaStream, String databaseServer) {
    try {
      String schemaString = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);

      NormalizedSchema schema = schemaAssistantChatClient.prompt()
          .user(u -> u.text(normalizeSchemaPrompt)
              .params(Map.of(
                  "database", originalSchema,
                  "schema", schemaString,
                  "database_server", databaseServer)))
          .call()
          .entity(new ParameterizedTypeReference<NormalizedSchema>() {
          });

      return schema;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read schema stream", e);
    }
  }

  @Override
  public List<String> getSyntheticDataAsCsv(Table table, int rowCount) {

    List<String> csvRows = schemaAssistantChatClient.prompt()
        .user(u -> u.text(generateSyntheticDataPrompt)
            .params(Map.of(
                "table_structure", table,
                "rowCount", rowCount)))
        .call()
        .entity(new ParameterizedTypeReference<List<String>>() {
        });

    return csvRows;
  }

  @Override
  public ByteArrayOutputStream generateSyntheticData(NormalizedSchema schema, int rowCount) throws IOException {

    ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();

    try (ZipOutputStream zos = new ZipOutputStream(zipBytes)) {

      for (NormalizedSchema.Table tbl : schema.tables()) {
        List<String> csvRows = getSyntheticDataAsCsv(tbl, rowCount);
        zos.putNextEntry(new ZipEntry(tbl.name() + ".csv"));

        // Use a Writer that **does not** close the ZipOutputStream
        Writer w = new OutputStreamWriter(zos); // no try‑with‑resources here
        for (String line : csvRows) {
          w.write(line);
          w.write('\n');
        }
        w.flush(); // make sure everything is written
        zos.closeEntry(); // close only the current entry
        // DO NOT call w.close() – that would close zos as well
      }
    }

    return zipBytes;
  }
}
