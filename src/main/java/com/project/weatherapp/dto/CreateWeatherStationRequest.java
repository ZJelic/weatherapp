package com.project.weatherapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateWeatherStationRequest(
        @NotBlank String name,
        String location,
        @NotNull Double latitude,
        @NotNull Double longitude,
        Double elevation
) {}
