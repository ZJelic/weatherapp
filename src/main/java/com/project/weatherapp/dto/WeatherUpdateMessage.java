package com.project.weatherapp.dto;

import java.time.LocalDateTime;

public record WeatherUpdateMessage(
        Long stationId,
        String stationName,
        double temperature,
        double windSpeed,
        LocalDateTime timestamp
) {}