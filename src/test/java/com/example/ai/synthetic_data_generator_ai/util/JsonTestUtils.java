package com.example.ai.synthetic_data_generator_ai.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonTestUtils {

  public static String getObjectAsPrettyJson(Object object, ObjectMapper objectMapper) {
    try {
      return objectMapper
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(object).strip();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static void removeFields(JsonNode node, String... fieldsToRemove) {
    if (node.isObject()) {
      ObjectNode objectNode = (ObjectNode) node;
      for (String field : fieldsToRemove) {
        if (objectNode.has(field)) {
          objectNode.remove(field);
        }
      }
      objectNode.properties().forEach(entry -> removeFields(entry.getValue(), fieldsToRemove));
    } else if (node.isArray()) {
      node.forEach(child -> removeFields(child, fieldsToRemove));
    }
  }
}
