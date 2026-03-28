package com.admin.web.controller;

import com.admin.web.model.ChatRequest;
import com.admin.web.model.ServerResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author znn
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public AiChatController(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatCompletions(@RequestBody ChatRequest chatRequest) {
        return this.chatClient.prompt()
                .user(chatRequest.question())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatRequest.conversationId()))
                .stream().chatResponse();
    }

    @DeleteMapping("/{conversationId}")
    public ServerResponse<?> clearHistory(@PathVariable String conversationId) {
        this.chatMemory.clear(conversationId);
        return ServerResponse.ok();
    }
}
