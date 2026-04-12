package com.admin.web.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * @author znn
 */
@Configuration
public class AiChatConfig {

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient chatClient(
            OpenAiChatModel chatModel,
            ChatMemory chatMemory,
            SystemPromptTemplate systemPromptTemplate) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder().build(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultSystem(systemPromptTemplate.render())
                .build();
    }

    @Bean
    public SystemPromptTemplate systemPromptTemplate(
            ConfigProperties configProperties,
            @Value("classpath:/system-prompt.md") Resource systemPrompt) {
        return SystemPromptTemplate.builder().resource(systemPrompt)
                .variables(Map.of(
                        "name", configProperties.getName(),
                        "version", configProperties.getVersion()
                )).build();
    }
}
