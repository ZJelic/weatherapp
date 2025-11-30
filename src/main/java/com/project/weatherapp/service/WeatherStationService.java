package com.project.weatherapp.service;

import com.project.weatherapp.dto.CreateWeatherStationRequest;
import com.project.weatherapp.dto.WeatherDataDTO;
import com.project.weatherapp.dto.WeatherStationDTO;

import java.util.List;

public interface WeatherStationService {
    WeatherStationDTO create(CreateWeatherStationRequest request);
    List<WeatherStationDTO> findAll();
    WeatherStationDTO findById(Long id);
    WeatherStationDTO update(Long id, CreateWeatherStationRequest request);
    List<WeatherStationDTO> findByArea(double latStart, double latEnd, double lonStart, double lonEnd);
    WeatherDataDTO getCurrentWeather(Long stationId);
}