package com.admin.web.model.request;

/**
 * @author znn
 */
public record ChatRequest(
        String conversationId,
        String question,
        boolean enableThinking,
        boolean enableSearch
) {
}
