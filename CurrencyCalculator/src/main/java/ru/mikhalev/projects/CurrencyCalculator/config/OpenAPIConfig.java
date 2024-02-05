package ru.mikhalev.projects.CurrencyCalculator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ivan Mikhalev
 */

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Currency calculator API")
                .version("1.0")
                .description("This API allows you to translate currencies at current and archive rates.");

        return new OpenAPI().info(info);
    }
}
