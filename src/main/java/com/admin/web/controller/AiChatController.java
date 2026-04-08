package com.admin.web.controller;

import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.ChatMemory;
import com.admin.web.model.ChatRequest;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.SysUserChat;
import com.admin.web.service.SysUserChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

/**
 * @author znn
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController extends BaseController {
    private final ChatClient chatClient;
    private final SysUserChatService sysUserChatService;

    public AiChatController(ChatClient chatClient, SysUserChatService sysUserChatService) {
        this.chatClient = chatClient;
        this.sysUserChatService = sysUserChatService;
    }

    @SysPermissions(SysLogin.class)
    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatCompletions(@RequestBody ChatRequest chatRequest) {
        this.sysUserChatService.save(super.getSysUser(), chatRequest.conversationId(), chatRequest.question());
        return this.chatClient.prompt()
                .user(chatRequest.question())
                .options(OpenAiChatOptions.builder()
                        .extraBody(Map.of(
                                "enable_thinking", chatRequest.enableThinking(),
                                "thinking", Map.of("type", chatRequest.enableThinking() ? "enabled" : "disabled"),
                                "enable_search", chatRequest.enableSearch(),
                                "web_search", Map.of("enable", chatRequest.enableSearch())
                        )).build())
                .advisors(a -> a.param(CONVERSATION_ID, chatRequest.conversationId()))
                .stream().chatResponse();
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/all")
    public ServerResponse<List<SysUserChat>> all(@RequestParam Integer page, @RequestParam Integer limit) {
        Slice<SysUserChat> sysUserChats = this.sysUserChatService
                .findByUsername(super.getSysUser(), PageRequest.of(page, limit));
        return ServerResponse.ok(sysUserChats.hasNext() ? 1L : 0L, sysUserChats.getContent());
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/all/{conversationId}")
    public ServerResponse<SysUserChat> all(@PathVariable String conversationId) {
        SysUserChat sysUserChat = this.sysUserChatService
                .findByUsernameAndConversationId(super.getSysUser(), conversationId);
        return ServerResponse.ok(sysUserChat);
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/{conversationId}")
    public ServerResponse<List<ChatMemory>> chatMemory(@PathVariable String conversationId) {
        List<ChatMemory> chatMemory = this.sysUserChatService.findByConversationId(conversationId);
        return ServerResponse.ok(chatMemory);
    }

    @SysPermissions(SysLogin.class)
    @PutMapping("/{conversationId}")
    public ServerResponse<?> update(@PathVariable String conversationId, @RequestBody String content) {
        this.sysUserChatService.update(super.getSysUser(), conversationId, content);
        return ServerResponse.ok();
    }

    @SysPermissions(SysLogin.class)
    @DeleteMapping("/{conversationId}")
    public ServerResponse<?> delete(@PathVariable String conversationId) {
        this.sysUserChatService.delete(super.getSysUser(), conversationId);
        return ServerResponse.ok();
    }
}
