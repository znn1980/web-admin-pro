package com.admin.web.service;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysWhois;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author znn
 */
@Service
public class SysWhoisService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public SysWhoisService(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public SysWhois whois(String ip) {
        return this.restClient.get()
                .uri("https://whois.pconline.com.cn/ipJson.jsp?json=true&ip={ip}", ip)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return this.objectMapper.readValue(StreamUtils.copyToString(response.getBody()
                                , Objects.requireNonNullElse(
                                        Objects.requireNonNullElse(
                                                response.getHeaders().getContentType()
                                                , MediaType.APPLICATION_JSON
                                        ).getCharset()
                                        , StandardCharsets.UTF_8
                                )
                        ), SysWhois.class);
                    }
                    throw new ServerResponseException(String.format("IP地址(%s)查询失败！(%s)", ip, response.getStatusCode()));
                });
    }
}
