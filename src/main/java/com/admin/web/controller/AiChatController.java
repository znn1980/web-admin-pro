package com.admin.web.controller;

import com.admin.web.model.ChatMemory;
import com.admin.web.model.ChatRequest;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.SysUserChat;
import com.admin.web.model.vo.PageVo;
import com.admin.web.service.SysUserChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

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

    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatCompletions(@RequestBody ChatRequest chatRequest) {
        this.sysUserChatService.save(super.getSysUser().getUsername()
                , chatRequest.conversationId(), chatRequest.question());
        return this.chatClient.prompt()
                .user(chatRequest.question())
                .advisors(a -> a.param(CONVERSATION_ID, chatRequest.conversationId()))
                .stream().chatResponse();
    }

    @PostMapping
    public ServerResponse<List<SysUserChat>> all(@RequestBody PageVo vo) {
        Slice<SysUserChat> sysUserChats = this.sysUserChatService.all(super.getSysUser().getUsername(), vo);
        return ServerResponse.ok(sysUserChats.getContent());
    }

    @GetMapping("{conversationId}")
    public ServerResponse<List<ChatMemory>> all(@PathVariable String conversationId) {
        List<ChatMemory> chatMemory = this.sysUserChatService.findByConversationId(conversationId);
        return ServerResponse.ok(chatMemory);
    }

    @DeleteMapping("/{conversationId}")
    public ServerResponse<?> delete(@PathVariable String conversationId) {
        this.sysUserChatService.deleteByConversationId(conversationId);
        return ServerResponse.ok();
    }
}
