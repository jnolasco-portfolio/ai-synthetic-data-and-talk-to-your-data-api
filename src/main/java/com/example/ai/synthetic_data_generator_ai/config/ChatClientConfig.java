package com.example.ai.synthetic_data_generator_ai.config;

import java.nio.charset.Charset;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatClientConfig {

  @Value("classpath:prompts/schema-assistant-system.st")
  private Resource schemaAssistantSystemPrompt;

  @Bean
  public ChatClient schemaAssistantChatClient(ChatClient.Builder builder) {
    return builder.defaultSystem(schemaAssistantSystemPrompt, Charset.defaultCharset())
        .build();
  }

}
