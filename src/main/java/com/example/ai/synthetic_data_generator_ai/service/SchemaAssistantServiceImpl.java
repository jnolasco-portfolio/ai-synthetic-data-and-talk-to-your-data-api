package com.example.ai.synthetic_data_generator_ai.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.NormalizedSchema;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchemaAssistantServiceImpl implements SchemaAssistantService {

  private final LLMSchemaAssistantClient llmSchemaAssistantClient;

  @Override
  public ByteArrayOutputStream generateSyntheticData(NormalizedSchema schema, int rowCount) throws IOException {

    ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();

    try (ZipOutputStream zos = new ZipOutputStream(zipBytes)) {

      for (NormalizedSchema.Table tbl : schema.tables()) {
        List<String> csvRows = llmSchemaAssistantClient.getSyntheticDataAsCsv(tbl, rowCount);
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
