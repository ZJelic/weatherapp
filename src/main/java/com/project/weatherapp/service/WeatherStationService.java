package com.project.weatherapp.service;

import com.project.weatherapp.dto.CreateWeatherStationRequest;
import com.project.weatherapp.dto.WeatherDataDTO;
import com.project.weatherapp.dto.WeatherStationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WeatherStationService {
    WeatherStationDTO create(CreateWeatherStationRequest request);
    Page<WeatherStationDTO> findAll(Pageable pageable);
    WeatherStationDTO findById(Long id);
    WeatherStationDTO update(Long id, CreateWeatherStationRequest request);
    List<WeatherStationDTO> findByArea(double latStart, double latEnd, double lonStart, double lonEnd);
    WeatherDataDTO getCurrentWeather(Long stationId);
}