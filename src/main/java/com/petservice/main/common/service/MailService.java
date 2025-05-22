package com.petservice.main.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService implements MailServiceInterface{

  @Value("${spring.mail.username}") private String FromMailAddress;

  private final JavaMailSender mailSender;

  @Async("mailTaskExecutor")
  @Retryable(
      value = { Exception.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000, multiplier = 2)
  )
  @Override
  public void sendMail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(FromMailAddress);  // 발신자
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    mailSender.send(message);
  }

  @Recover
  public void Recover(Exception e, String to, String subject, String body){
    System.err.println(
        String.format("메일 전송 재시도 실패: to=%s, subject=%s, error=%s", to, subject, e.getMessage())
    );

  }

}
