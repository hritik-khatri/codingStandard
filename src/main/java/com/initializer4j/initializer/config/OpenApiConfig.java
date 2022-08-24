package com.initializer4j.initializer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi(
      @Value("${application-description}") String appDescription,
      @Value("${application-version}") String appVersion) {
    log.debug("Open API bean");
    return new OpenAPI()
        .info(
            new Info()
                .title("initializer4j application Open API")
                .version(appVersion)
                .description(appDescription)
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  }
}
