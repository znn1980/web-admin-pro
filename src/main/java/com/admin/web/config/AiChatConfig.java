package com.admin.web.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel, ChatMemory chatMemory, SystemPromptTemplate defaultSystem) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder().build(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultSystem(defaultSystem.render())
                .build();
    }

    @Bean
    public SystemPromptTemplate defaultSystem(
            @Value("${spring.application.name}") String name,
            @Value("${spring.application.version}") String version,
            @Value("classpath:/static/default-system.md") Resource defaultSystem) {
        return SystemPromptTemplate.builder().resource(defaultSystem)
                .variables(Map.of("name", name, "version", version)).build();
    }
}
