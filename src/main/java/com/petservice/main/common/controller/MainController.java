package com.petservice.main.common.controller;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class MainController {

  @PermitAll
  @GetMapping({"/", "/{path:^(?!api|error)[^\\.]*}"})
  public String index() {
    return "forward:/index.html";
  }
}
