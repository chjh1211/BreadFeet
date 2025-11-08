package com.example.breadfeet_BE.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI breadfeetOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🍞 BreadFeet API 문서")
                        .description("BreadFeet 프로젝트의 REST API 문서입니다.")
                        .version("v1.0.0")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"))
                );
    }
}
