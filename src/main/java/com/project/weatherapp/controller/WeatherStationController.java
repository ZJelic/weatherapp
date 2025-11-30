package com.project.weatherapp.controller;

import com.project.weatherapp.dto.CreateWeatherStationRequest;
import com.project.weatherapp.dto.WeatherDataDTO;
import com.project.weatherapp.dto.WeatherStationDTO;
import com.project.weatherapp.service.WeatherStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/weather-stations")
@RequiredArgsConstructor
public class WeatherStationController {

    private final WeatherStationService service;

    @PostMapping
    public ResponseEntity<WeatherStationDTO> create(@RequestBody CreateWeatherStationRequest request) {
        log.info("Received request to create weather station with name: {}", request.name());
        WeatherStationDTO created = service.create(request);
        log.info("Created weather station with ID: {}", created.id());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<WeatherStationDTO>> getAll() {
        log.info("Received request to fetch all weather stations");
        List<WeatherStationDTO> stations = service.findAll();
        log.info("Returning {} weather stations", stations.size());
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherStationDTO> getOne(@PathVariable Long id) {
        log.info("Received request to fetch weather station with ID: {}", id);
        WeatherStationDTO station = service.findById(id);
        log.info("Returning weather station: {}", station);
        return ResponseEntity.ok(station);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeatherStationDTO> update(
            @PathVariable Long id,
            @RequestBody CreateWeatherStationRequest request) {
        log.info("Received request to update weather station with ID: {}", id);
        WeatherStationDTO updated = service.update(id, request);
        log.info("Updated weather station with ID: {}", updated.id());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public ResponseEntity<List<WeatherStationDTO>> findByArea(
            @RequestParam double latStart,
            @RequestParam double latEnd,
            @RequestParam double lonStart,
            @RequestParam double lonEnd) {
        log.info("Received request to search weather stations in area: lat({}-{}), lon({}-{})",
                latStart, latEnd, lonStart, lonEnd);
        List<WeatherStationDTO> stations = service.findByArea(latStart, latEnd, lonStart, lonEnd);
        log.info("Found {} stations in area", stations.size());
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}/weather")
    public ResponseEntity<WeatherDataDTO> getLiveWeather(@PathVariable Long id) {
        log.info("Received request for live weather for station {}", id);
        return ResponseEntity.ok(service.getCurrentWeather(id));
    }

}