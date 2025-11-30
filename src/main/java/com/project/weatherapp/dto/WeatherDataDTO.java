package com.project.weatherapp.dto;

public record WeatherDataDTO(
        double temperature,
        double windspeed,
        double winddirection,
        int weathercode,
        String time
)  {}
