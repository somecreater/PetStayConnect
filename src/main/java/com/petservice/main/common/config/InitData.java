package com.petservice.main.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {

  private final JdbcTemplate jdbcTemplate;

  @Value("${app.sql.filepath}")
  private String SqlFilePath;
  @Value("${app.sql.local}")
  private String Local;

  @Override
  public void run(ApplicationArguments args) throws Exception {

  }
}
