package com.admin.web.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;

/**
 * @author znn
 */
@Configuration
public class HttpClientConfiguration {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientConfiguration.class);
    private final static int TIMEOUT_SECONDS = 10;
    private final static int ONE_SECONDS = 1000;

    @Bean
    public SslContext sslContext() throws SSLException {
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
    }

    @Bean
    public HttpClient httpClient(SslContext sslContext) {
        return HttpClient.create(ConnectionProvider.create("custom"))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_SECONDS * ONE_SECONDS)
                .responseTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .doOnConnected(conn -> conn
                        .addHandlerFirst(new ReadTimeoutHandler(TIMEOUT_SECONDS))
                        .addHandlerFirst(new WriteTimeoutHandler(TIMEOUT_SECONDS)))
                .doOnRequest((request, conn) ->
                        logger.info("Request: {} {} {}", request.version(), request.method(), request.uri()))
                .doOnResponse((response, conn) ->
                        logger.info("Response status: {} headers: {}", response.status(), response.responseHeaders()))
                .secure(ssl -> ssl.sslContext(sslContext));
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public RestClient restClient(HttpClient httpClient) {
        return RestClient.builder()
                .requestFactory(new ReactorClientHttpRequestFactory(httpClient))
                .build();
    }
}
