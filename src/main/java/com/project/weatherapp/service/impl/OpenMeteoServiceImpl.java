package com.project.weatherapp.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.weatherapp.dto.WeatherDataDTO;
import com.project.weatherapp.exception.ExternalApiException;
import com.project.weatherapp.service.OpenMeteoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenMeteoServiceImpl implements OpenMeteoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    /**
     * Fetches current weather data from Open-Meteo API with retry support.
     * Retries up to 3 times on RestClientException with 2s delay.
     */
    @Override
    @Retryable(
            value = {RestClientException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public WeatherDataDTO getCurrentWeather(double latitude, double longitude) {

        validateCoordinates(latitude, longitude);

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                latitude, longitude
        );

        log.info("Calling Open-Meteo API: {}", url);

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode current = mapper.readTree(json).path("current_weather");

            if (current.isMissingNode()) {
                throw new ExternalApiException("Missing 'current_weather' in Open-Meteo response");
            }

            return new WeatherDataDTO(
                    current.path("temperature").asDouble(),
                    current.path("windspeed").asDouble(),
                    current.path("winddirection").asDouble(),
                    current.path("weathercode").asInt(),
                    current.path("time").asText()
            );

        } catch (RestClientException ex) {
            log.error("Open-Meteo API call failed for lat={}, lon={}", latitude, longitude, ex);
            throw new ExternalApiException("Failed to retrieve data from Open-Meteo API", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while parsing Open-Meteo response", ex);
            throw new ExternalApiException("Unexpected error processing Open-Meteo API response", ex);
        }
    }

    // Recovery method called when retries are exhausted.
    @Recover
    public WeatherDataDTO recover(RestClientException ex, double latitude, double longitude) {
        log.error("Open-Meteo API unavailable after retries at lat={}, lon={}", latitude, longitude, ex);
        throw new ExternalApiException("Open-Meteo API unavailable after retries", ex);
    }

    private void validateCoordinates(double lat, double lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
}
