package com.statemachinedemo.springstatemachinedemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author anhdt9
 * @since 10/12/24
 */
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Campaign State Machine API")
                .version("1.0")
                .description("API documentation for managing campaigns and their states"));
  }
}
