package com.project.weatherapp.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.weatherapp.dto.WeatherDataDTO;
import com.project.weatherapp.service.OpenMeteoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenMeteoServiceImpl implements OpenMeteoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Override
    public WeatherDataDTO getCurrentWeather(double latitude, double longitude) {

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                latitude, longitude
        );

        log.info("Calling Open-Meteo API: {}", url);

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode current = mapper.readTree(json).path("current_weather");

            if (current.isMissingNode()) {
                throw new RestClientException("Open-Meteo API response missing 'current_weather'");
            }

            return new WeatherDataDTO(
                    current.path("temperature").asDouble(),
                    current.path("windspeed").asDouble(),
                    current.path("winddirection").asDouble(),
                    current.path("weathercode").asInt(),
                    current.path("time").asText()
            );

        } catch (RestClientException ex) {
            log.error("Open-Meteo API failed for lat={}, lon={}", latitude, longitude, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error calling Open-Meteo API", ex);
            throw new RestClientException("Open-Meteo call failed", ex);
        }
    }
}
