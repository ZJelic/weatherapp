package com.project.weatherapp.service;

import com.project.weatherapp.dto.WeatherDataDTO;

public interface OpenMeteoService {
    WeatherDataDTO getCurrentWeather(double latitude, double longitude);
}
