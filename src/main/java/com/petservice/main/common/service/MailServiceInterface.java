package com.petservice.main.common.service;

public interface MailServiceInterface {

  public void sendMail(String to, String subject, String text);
  public void Recover(Exception e, String to, String subject, String body);
}
