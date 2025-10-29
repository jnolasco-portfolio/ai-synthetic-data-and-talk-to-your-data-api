package com.example.ai.synthetic_data_generator_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

  @Bean
  public ChatClient schemaAssistantChatClient(ChatClient.Builder builder) {
    return builder.defaultSystem("You are a expert assistant on database schemas.")
        .build();
  }

}
