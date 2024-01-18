package com.example.filmBooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {
    @Bean
    OpenAPI insmartOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CMS Api")
                        .description("CMS api documentation for PDA")
                        .version("0.0.1")
                );
    }
}
