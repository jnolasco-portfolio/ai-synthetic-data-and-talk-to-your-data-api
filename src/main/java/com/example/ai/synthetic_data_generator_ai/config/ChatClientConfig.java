package com.example.ai.synthetic_data_generator_ai.config;

import java.nio.charset.Charset;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatClientConfig {

  @Value("classpath:prompts/schema-assistant-system.st")
  private Resource schemaAssistantSystemPrompt;

  @Bean
  public ChatMemory chatMemory() {
    return MessageWindowChatMemory.builder().build();
  }

  @Bean
  public ChatClient schemaAssistantChatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
    return builder
        .defaultSystem(schemaAssistantSystemPrompt, Charset.defaultCharset())
        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
        .build();
  }

}
