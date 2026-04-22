package com.api.RPGGameEngine.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RPG Game Engine API")
                        .description("API REST de gestion de personnages et d'items dans un univers RPG")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RPG Game Engine")
                                .email("anaestasia.mathieu@gmail.com")));
    }
}