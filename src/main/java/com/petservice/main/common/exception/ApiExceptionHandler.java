package com.petservice.main.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

  @ExceptionHandler({
      IllegalArgumentException.class,
      HttpMessageNotReadableException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String,Object> handleBadRequest(Exception ex) {
    log.error("400 예외 발생: {}", ex.getMessage());
    return Map.of(
        "status", 400,
        "error",   "Bad Request",
        "message", ex.getMessage()
    );
  }

  @ExceptionHandler({
      AuthenticationException.class,
      UsernameNotFoundException.class
  })
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Map<String,Object> handleUnauthorized(AuthenticationException ex) {
    log.error("401 예외 발생: {}", ex.getMessage());
    return Map.of(
        "status", 401,
        "error",   "Unauthorized",
        "message", "로그인이 필요합니다."
    );
  }

  @ExceptionHandler(PaymentRequiredException.class)
  @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
  public Map<String,Object> handlePaymentRequired(PaymentRequiredException ex) {
    log.error("402 예외 발생: {}", ex.getMessage());
    return Map.of(
        "status", 402,
        "error",   "Payment Required",
        "message", ex.getMessage()
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Map<String,Object> handleForbidden(AccessDeniedException ex) {
    log.error("403 예외 발생: {}", ex.getMessage());
    return Map.of(
        "status", 403,
        "error",   "Forbidden",
        "message", "권한이 없습니다."
    );
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String,Object> handleNotFound(NoHandlerFoundException ex) {
    log.error("404 예외 발생: {}", ex.getMessage());
    return Map.of(
        "status", 404,
        "error",   "Not Found",
        "message", "요청하신 리소스를 찾을 수 없습니다."
    );
  }


  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String,Object> handleServerError(Exception ex) {
    log.error("500 예외 발생: {}", ex.getMessage());
    return Map.of(
        "status", 500,
        "error",   "Internal Server Error",
        "message", "서버에 오류가 발생했습니다."
    );
  }

}
