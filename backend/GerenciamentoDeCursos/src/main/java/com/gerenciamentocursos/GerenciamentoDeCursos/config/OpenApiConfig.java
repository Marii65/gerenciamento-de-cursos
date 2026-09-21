package com.gerenciamentocursos.GerenciamentoDeCursos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Gerenciamento de Cursos")
                        .version("1.0")
                        .description("API desenvolvida para o processo seletivo da Support. Permite a gestão completa de alunos, cursos e matrículas.")
                        .contact(new Contact()
                                .name("Maria Luiza Santos Nascimento")
                                .email("malunascimento672@gmail.com")));
    }
}
