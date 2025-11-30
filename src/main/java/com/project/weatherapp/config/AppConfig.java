package com.project.weatherapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * Provides a RestTemplate bean for making HTTP calls.
     * Can be injected anywhere in the app instead of using 'new RestTemplate()'.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Provides a Jackson ObjectMapper bean.
     * Registers JavaTimeModule to correctly serialize/deserialize Java 8+ date/time types (LocalDateTime, ZonedDateTime, etc.).
     * Can be injected anywhere instead of creating a new ObjectMapper manually.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Disable writing dates as timestamps (instead get ISO strings)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
