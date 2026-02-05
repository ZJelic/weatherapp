package com.project.weatherapp.repository;

import com.project.weatherapp.entity.WeatherUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherUpdateRepository extends JpaRepository<WeatherUpdate, Long> {
}
