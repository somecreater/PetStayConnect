package com.petservice.main.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry
public class AsyncThreadConfig {

  @Value("${async.mail.core-pool-size}")
  private Integer core_pool_size;
  @Value("${async.mail.max-pool-size}")
  private Integer max_pool_size;
  @Value("${async.mail.queue-capacity}")
  private Integer queue_capacity;

  @Bean(name = "mailTaskExecutor")
  public Executor mailTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(core_pool_size);
    executor.setMaxPoolSize(max_pool_size);
    executor.setQueueCapacity(queue_capacity);
    executor.setThreadNamePrefix("MailExecutor-");
    executor.initialize();
    return executor;
  }
}
