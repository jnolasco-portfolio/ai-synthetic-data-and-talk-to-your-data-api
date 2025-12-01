package com.example.ai.synthetic_data_generator_ai.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ai.synthetic_data_generator_ai.dto.QueryRequest;
import com.example.ai.synthetic_data_generator_ai.llm.LLMSchemaAssistantClient;
import com.example.ai.synthetic_data_generator_ai.service.DynamicDataService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TalkToYourDataController.class)
public class TalkToYourDataRestaurantsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DynamicDataService dynamicDataService;

    @MockitoBean
    LLMSchemaAssistantClient llmSchemaAssistantClient;

    @Test
    void testQuery() throws Exception {
        QueryRequest request = new QueryRequest("test", "restaurants_schema",
                "How many restaurants are there in New York?");

        when(dynamicDataService.getDatabaseSchema(any())).thenReturn(null);
        when(llmSchemaAssistantClient.generateSqlQuery(any(), any(), any()))
                .thenReturn(new com.example.ai.synthetic_data_generator_ai.llm.LLMQueryResponse("SELECT COUNT(*) FROM restaurants WHERE city = 'New York'", null));
        when(dynamicDataService.executeQuery(any(), any())).thenReturn(List.of(Map.of("count", 10L)));

        mockMvc.perform(post("/api/v1/data-conversations/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
