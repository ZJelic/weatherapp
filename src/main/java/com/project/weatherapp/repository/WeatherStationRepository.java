package com.project.weatherapp.repository;

import com.project.weatherapp.entity.WeatherStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherStationRepository extends JpaRepository<WeatherStation, Long> {

    //find weather stations in a specific area
    List<WeatherStation> findByLatitudeBetweenAndLongitudeBetween(double latStart, double latEnd, double lonStart, double lonEnd);
}
