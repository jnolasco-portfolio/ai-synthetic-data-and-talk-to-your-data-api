package com.example.ai.synthetic_data_generator_ai.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;
import com.example.ai.synthetic_data_generator_ai.dto.QueryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TalkToYourDataControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Value("classpath:data/learn-schema-response.json")
        private Resource exampleSchemaJson;

        @Test
        void testQuery() throws Exception {
                LearnDatabaseResponse testSchema = objectMapper
                                .readValue(exampleSchemaJson.getContentAsString(Charset.defaultCharset()),
                                                LearnDatabaseResponse.class);

                QueryRequest request = new QueryRequest(
                                "Show me oldest Author",
                                "123",
                                "library");

                String expectedSqlQuery = "SELECT first_name, last_name, birth_date FROM Authors ORDER BY birth_date ASC LIMIT 1";

                mockMvc.perform(post("/api/v1/schema-assistant/questions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.metadata.content_type").value("table"))
                                .andExpect(jsonPath("$.sqlQuery").value(expectedSqlQuery))
                                .andExpect(jsonPath("$.result[0].first_name").value("Homer"));
        }
}
