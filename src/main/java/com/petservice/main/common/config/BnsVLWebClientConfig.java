package com.petservice.main.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Configuration
public class BnsVLWebClientConfig {

  @Value("${business.api.base-url}")
  private String baseUrl;

  @Value("${business.api.secret-key}")
  private String clientSecret;

  @Bean
  public WebClient businessWebClient() {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader("serviceKey", clientSecret)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
        .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
        .build();
  }
}
