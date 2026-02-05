package com.project.weatherapp.service.impl;

import com.project.weatherapp.dto.CreateWeatherStationRequest;
import com.project.weatherapp.dto.WeatherDataDTO;
import com.project.weatherapp.dto.WeatherStationDTO;
import com.project.weatherapp.dto.WeatherUpdateMessage;
import com.project.weatherapp.entity.WeatherStation;
import com.project.weatherapp.exception.NotFoundException;
import com.project.weatherapp.mapper.WeatherStationMapper;
import com.project.weatherapp.repository.WeatherStationRepository;
import com.project.weatherapp.service.OpenMeteoService;
import com.project.weatherapp.service.WeatherStationService;
import com.project.weatherapp.kafka.WeatherProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherStationServiceImpl implements WeatherStationService {

    private final WeatherStationRepository repository;
    private final WeatherStationMapper mapper;
    private final OpenMeteoService openMeteoService;
    private final WeatherProducerService weatherProducerService; // Kafka producer

    @Override
    public WeatherStationDTO create(CreateWeatherStationRequest request) {
        log.info("Creating weather station with name: {}", request.name());
        WeatherStation entity = mapper.toEntity(request);
        WeatherStation saved = repository.save(entity);
        log.info("Weather station saved with ID: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public List<WeatherStationDTO> findAll() {
        log.info("Fetching all weather stations");
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WeatherStationDTO findById(Long id) {
        log.info("Fetching weather station with ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Weather station with ID " + id + " not found"));
    }

    @Override
    public WeatherStationDTO update(Long id, CreateWeatherStationRequest request) {
        log.info("Updating weather station with ID: {}", id);
        WeatherStation entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Weather station with ID " + id + " not found"));
        log.info("Before update: {}", entity);
        mapper.updateFromRequest(request, entity);
        WeatherStation updated = repository.save(entity);
        log.info("After update: {}", updated);
        return mapper.toDTO(updated);
    }

    @Override
    public List<WeatherStationDTO> findByArea(double latStart, double latEnd, double lonStart, double lonEnd) {
        log.info("Searching weather stations in area: lat({}-{}), lon({}-{})", latStart, latEnd, lonStart, lonEnd);
        List<WeatherStationDTO> stations = repository
                .findByLatitudeBetweenAndLongitudeBetween(latStart, latEnd, lonStart, lonEnd)
                .stream()
                .map(mapper::toDTO)
                .toList();

        if (stations.isEmpty()) {
            throw new NotFoundException(
                    String.format("No weather stations found in area lat(%s-%s) lon(%s-%s)", latStart, latEnd, lonStart, lonEnd)
            );
        }

        return stations;
    }

    @Override
    @Cacheable(value = "weather", key = "#stationId")
    public WeatherDataDTO getCurrentWeather(Long stationId) {

        WeatherStation ws = repository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("Station not found"));

        log.info("Fetching weather for station {} ({}, {})",
                ws.getName(), ws.getLatitude(), ws.getLongitude());

        try {
            WeatherDataDTO weather =
                    openMeteoService.getCurrentWeather(ws.getLatitude(), ws.getLongitude());

            log.info("Received weather for station {}: {}", ws.getName(), weather);

            WeatherUpdateMessage event = new WeatherUpdateMessage(
                    ws.getId(),
                    ws.getName(),
                    weather.temperature(),
                    weather.windspeed(),
                    LocalDateTime.now()
            );

            weatherProducerService.sendWeatherUpdate(event);

            return weather;

        } catch (RestClientException ex) {
            log.error("Open-Meteo API failed for station {} ({}, {})",
                    ws.getName(), ws.getLatitude(), ws.getLongitude(), ex);
            throw ex;
        }
    }

}
