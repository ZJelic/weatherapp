package com.project.weatherapp.mapper;

import com.project.weatherapp.dto.WeatherStationDTO;
import com.project.weatherapp.dto.CreateWeatherStationRequest;
import com.project.weatherapp.entity.WeatherStation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WeatherStationMapper {

    WeatherStationDTO toDTO(WeatherStation entity);

    WeatherStation toEntity(CreateWeatherStationRequest request);

    void updateFromRequest(CreateWeatherStationRequest request, @MappingTarget WeatherStation entity);
}
