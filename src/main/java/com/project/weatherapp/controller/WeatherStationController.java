package com.project.weatherapp.controller;

import com.project.weatherapp.dto.CreateWeatherStationRequest;
import com.project.weatherapp.dto.WeatherDataDTO;
import com.project.weatherapp.dto.WeatherStationDTO;
import com.project.weatherapp.service.WeatherStationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<WeatherStationDTO> create(
            @Valid @RequestBody CreateWeatherStationRequest request) {

        log.info("Received request to create weather station with name: {}", request.name());
        WeatherStationDTO created = service.create(request);
        log.info("Created weather station with ID: {}", created.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<Page<WeatherStationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        size = Math.min(size, 50);

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherStationDTO> getOne(@PathVariable Long id) {
        log.info("Received request to fetch weather station with ID: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeatherStationDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateWeatherStationRequest request) {

        log.info("Received request to update weather station with ID: {}", id);
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<WeatherStationDTO>> findByArea(
            @RequestParam double latStart,
            @RequestParam double latEnd,
            @RequestParam double lonStart,
            @RequestParam double lonEnd) {

        log.info("Searching stations in area");
        return ResponseEntity.ok(
                service.findByArea(latStart, latEnd, lonStart, lonEnd)
        );
    }

    @GetMapping("/{id}/weather")
    public ResponseEntity<WeatherDataDTO> getLiveWeather(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCurrentWeather(id));
    }
}
