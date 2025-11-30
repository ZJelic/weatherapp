package com.project.weatherapp.dto;

import lombok.Builder;

@Builder
public record WeatherStationDTO(
        Long id,
        String name,
        String location,
        Double latitude,
        Double longitude,
        Double elevation
) {}
