package com.petservice.main.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Value("${spring.web.resources.add-mappings}")
  private boolean is_mapping;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if(!is_mapping) {
      registry.addResourceHandler("/index.html","/assets/**","/favicon.ico", "/*.png", "/*.svg",
         "/*.jpg", "/*.html","/*.js", "/*.css", "/asset-manifest.json", "/static/**")
        .addResourceLocations("classpath:/static/assets/", "classpath:/static/");
    }
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry){
    registry.addViewController("/{spring:[^.]+}")
        .setViewName("forward:/index.html");
    registry.addViewController("/**/{spring:[^.]+}")
        .setViewName("forward:/index.html");
  }
}
